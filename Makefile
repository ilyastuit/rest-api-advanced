up: docker-up
down: docker-down
restart-app: build-app restart-app-container
init: build-app docker-build docker-up migrate

build-app:
	./gradlew clean build -x test --stacktrace

build-app-test:
	./gradlew clean build --stacktrace

test:
	./gradlew test --stacktrace

docker-build:
	docker-compose build

migrate:
	./gradlew flywayClean flywayMigrate --stacktrace

generate-data:
	java -jar ./cli/build/libs/rest-cli.jar clear=true generate=true

docker-up:
	docker-compose up -d

docker-down:
	docker-compose down --remove-orphans

restart-app-container:
	docker-compose restart application