#! /bin/bash

cont_name="jrvs-psql"
request=$1
username=$2
password=$3

#validate number of arguments
if [ "$#" -ge 4 ]; then
	echo "Error: Illegal number of parameters"
	exit 1
fi

#process request
if [ "$request" == "create" ]; then
	#check for existing container
	systemctl status docker || systemctl start docker
	cont_status=$(docker container ls -a -f name=$cont_name | wc -l)

	if [ "$cont_status" -eq 2 ]; then
		echo "Error: Container already created"
		exit 1
	fi

	#check for auth details
	if [ -z "$username" ]; then
		echo "Error: No username provided"
		exit 1
	elif [ -z "$password" ]; then
		echo "Error: No password provided"
		exit 1
	fi

	#create container
	docker pull postgres
	docker volume create pgdata
	docker run --name $cont_name -e POSTGRES_USER=$username -e POSTGRES_PASSWORD=$password -d -v pgdata:/var/lib/postgresql/data -p 5432:5432 postgres
	exit $?

#carry out start or stop
elif [ "$request" == "start" ]; then
	docker container start $cont_name || echo "Error: Container not created"
	exit $?
elif [ "$request" == "stop" ]; then
	docker container stop $cont_name || echo "Error: Container not created"
	exit $?
else
	echo "Error: Invalid request"
	exit 1
fi

exit 0
