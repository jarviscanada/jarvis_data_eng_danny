#! /bin/bash

#setting args as variables
psql_host=$1
psql_port=$2
db_name=$3
psql_user=$4
psql_password=$5

#validate number of arguments
if [ "$#" -ge 6 ]; then
	echo "Error: Illegal number of parameters"
	exit 1
fi

#reading usage info into variables
hostname=$(hostname -f)
vmstats=$(vmstat -t)
vmstats_disk=$(vmstat -d)
mem_specs=$(cat /proc/meminfo)
disk_info=$(df -BM /)

#extract usage stats
memory_free=$(echo "$mem_specs" | egrep "^MemFree:" | awk -F':' '{print $NF}' |
sed 's/.\{2\}$//' | xargs)
cpu_idle=$(echo "$vmstats" | awk 'FNR == 3 {print $13}') #us field
cpu_kernel=$(echo "$vmstats" | awk 'FNR == 3 {print $14}') #sy field
disk_io=$(echo "$vmstats_disk" | awk 'FNR == 3 {print $10}')
disk_available=$(echo "$disk_info" | awk 'FNR == 2 {print $4}' | sed 's/.$//')
#extract timestamp with regex
timestamp=$(echo "$vmstats" | egrep -o '[0-9]{4}-[0-9]{2}-[0-9]{2}\s[0-9]{2}:[0-9]{2}:[0-9]{2}')

#construct SQL statement
subquery="(SELECT id FROM host_info WHERE hostname = '${hostname}')"
statement="INSERT INTO host_usage (
	timestamp, 
	host_id, 
	memory_free, 
	cpu_idle, 
	cpu_kernel, 
	disk_io, 
	disk_available)
	VALUES (
	'${timestamp}',
	${subquery},
	${memory_free},
	${cpu_idle},
	${cpu_kernel},
	${disk_io},
	${disk_available});"

#weird issue with PSQL ignoring -W flag,  working around it by using env var
export PGPASSWORD=$psql_password
psql -h $psql_host -p $psql_port -U $psql_user -d $db_name -c "$statement"

exit 0
