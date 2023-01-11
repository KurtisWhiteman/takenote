Write-Host "Stopping the current running Port container"
docker-compose -f local-docker-compose.yml stop port-dev
Write-Host "Building the Port backend jar"
Set-Location -Path '..'
mvn -pl backend -am clean package -DskipTests
Copy-Item -Path "backend\target\backend*.jar" -Destination "."
Set-Location -Path 'scrum'
Write-Host "Building the Port docker container"
docker-compose -f local-docker-compose.yml build port-dev
Write-Host "Starting the Port docker container"
docker-compose -f local-docker-compose.yml up -d port-dev
