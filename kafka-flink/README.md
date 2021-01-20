Commannds:
docker exec -it 2639a55f70b6 ./bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic flink-demo

kafka-server-start /usr/local/etc/kafka/server.properties
zookeeper-server-start /usr/local/etc/kafka/zookeeper.properties
kafka-topics --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic flink-demo
kafka-topics --zookeeper localhost:2181 --delete --topic flink-dem

https://github.com/tgrall/kafka-flink-101
