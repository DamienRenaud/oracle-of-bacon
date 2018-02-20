#!/bin/sh

wget http://bit.ly/imdbdataset
unzip imdbdataset
neo4j-admin import --nodes:Movies imdb-data/movies.csv --nodes:Actors imdb-data/actors.csv --relationships imdb-data/roles.csv
neo4j restart

exit 0