#!/usr/bin/env bash

curl -XDELETE 'localhost:9200/imdb?pretty'

curl -XPUT 'localhost:9200/imdb?pretty' -H 'Content-Type: application/json' -d'
{
 "mappings": {
   "actors": {
     "properties": {
       "suggest": {
         "type": "completion"
       },
       "name": {
         "type": "keyword"
       }
     }
   }
 }
}
'

#curl -XPOST 'http://localhost:9200/imdb/actors' -H 'Content-Type: application/json' -d '{
#	"name" : "Jonh, Doe",
#	"suggest": ["Jonh", "Doe", "Jonh Doe"]
#}'
