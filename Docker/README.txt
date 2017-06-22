#### START WITH
	docker-compose up --build
	
#### STOP WITH
	either Ctrl+C or docker-compose down then do some clean up (e.g. docker-compose rm -f, docker volume prune, ...)
	
#### NOTE
	- to change the number of cluster masters, adapt the docker-compose.yml file
	- assign new GUIDs for MYID
	- add an addition queue MYQUEUE & database DB_NAME
	- adapt the GATEWAYCOUNT of the StatusService
	- create more databases in ./CustomDB/create_db.sh
