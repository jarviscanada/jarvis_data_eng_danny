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

#store hardware specs into variables
cpu_specs=`lscpu`
mem_specs=$(cat /proc/meminfo)
hostname=$(hostname -f)
vmstats=$(vmstat -t)

#parse specs for transport
cpu_number=$(echo "$cpu_specs" | egrep "^CPU\(s\):" | awk -F':' '{print $NF}' | xargs)
cpu_architecture=$(echo "$cpu_specs" | egrep "^Architecture:" | awk -F':' '{print $NF}' | xargs)
cpu_model=$(echo "$cpu_specs" | egrep "^Model name:" | awk -F':' '{print $NF}' | xargs)
cpu_mhz=$(echo "$cpu_specs" | egrep "^CPU MHz:" | awk -F':' '{print $NF}' | xargs)
l2_cache=$(echo "$cpu_specs" | egrep "^L2 cache:" | awk -F':' '{print $NF}' | sed 's/.$//' | xargs)
total_mem=$(echo "$mem_specs" | egrep "^MemTotal:" | awk '{print $2}' | xargs)
#extract timestamp with regex
timestamp=$(echo "$vmstats" | egrep -o '[0-9]{4}-[0-9]{2}-[0-9]{2}\s[0-9]{2}:[0-9]{2}:[0-9]{2}')

#construct SQL statement
statement="INSERT INTO host_info (
	hostname, 
	cpu_number, 
	cpu_architecture, 
	cpu_model, 
	cpu_mhz, 
	l2_cache, 
	total_mem, 
	timestamp) 
	VALUES (
	'${hostname}', 
	${cpu_number}, 
	'${cpu_architecture}', 
	'${cpu_model}', 
	${cpu_mhz}, 
	${l2_cache}, 
	${total_mem}, 
	'${timestamp}');"

#execute command
export PGPASSWORD=$psql_password
psql -h $psql_host -p $psql_port -U $psql_user -d $db_name -c "$statement"

exit 0
