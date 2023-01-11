Write-Host "Stopping the mysql docker database"
docker-compose stop database
Write-Host "Deleting the mysql docker database"
docker-compose rm -f database
Write-Host "Rebuilding the mysql docker database"
docker-compose build database
Write-Host "Starting the mysql docker database"
docker-compose up -d database
