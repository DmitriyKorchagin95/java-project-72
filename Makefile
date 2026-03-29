# Makefile
run-dist: # run app
	cd app  && ./gradlew clean installDist
	./app/build/install/app/bin/app

build: # build app
	cd app  && ./gradlew clean build

.PHONY: build