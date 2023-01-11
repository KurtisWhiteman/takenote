Write-Host "Building the mysql docker database"
docker-compose build database
Write-Host "Starting the mysql docker database"
docker-compose up -d database
