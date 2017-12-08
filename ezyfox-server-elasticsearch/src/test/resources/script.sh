curl -XPUT "http://localhost:9200/test" -d'
{
    "settings" : {
        "number_of_shards" : 1
    },
    "mappings" : {
        "one" : {
            "properties" : {
                "foo" : { "type" : "text" },
                "bar" : { "type" : "text" }
            }
        }
    }
}'