rem download kafka at  https:\\www.apache.org\dyn\closer.cgi?path=\kafka\0.11.0.0\kafka_2.11-0.11.0.0.tgz
rem extract kafka in a folder

rem WINDOWS ONLY

rem open a shell - zookeeper is at localhost:2181
bin\windows\zookeeper-server-start.bat config\zookeeper.properties

rem open another shell - kafka is at localhost:9092
bin\windows\kafka-server-start.bat config\server.properties

rem create input topic
bin\windows\kafka-topics.bat --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic streams-plaintext-input
bin\windows\kafka-topics.bat --create --zookeeper localhost:2181 --replication-factor 1 --partitions 2 --topic word-count-input
bin\windows\kafka-topics.bat --create --zookeeper localhost:2181 --replication-factor 1 --partitions 2 --topic word-count-output
bin\windows\kafka-topics.bat --create --zookeeper localhost:2181 --replication-factor 1 --partitions 2 --topic bank-transactions
bin\windows\kafka-topics.bat --create --zookeeper localhost:2181 --replication-factor 1 --partitions 3 --topic user-purchasess



bin\windows\kafka-topics.bat --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic favourite-colour-input
bin\windows\kafka-topics.bat --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic user-keys-and-colours --config cleanup.policy=compact
bin\windows\kafka-topics.bat --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic favourite-colour-output --config cleanup.policy=compact
bin\windows\kafka-topics.bat --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic bank-balance-exactly-once --config cleanup.policy=compact
bin\windows\kafka-topics.bat --create --zookeeper localhost:2181 --replication-factor 1 --partitions 2 --topic user-table --config cleanup.policy=compact
bin\windows\kafka-topics.bat --create --zookeeper localhost:2181 --replication-factor 1 --partitions 2 --topic user-purchases-enriched-inner-join --config cleanup.policy=compact
bin\windows\kafka-topics.bat --create --zookeeper localhost:2181 --replication-factor 1 --partitions 2 --topic user-purchases-enriched-left-join --config cleanup.policy=compact
bin\windows\kafka-topics.bat --create --zookeeper localhost:2181 --replication-factor 1 --partitions 2 --topic user-purchases --config cleanup.policy=compact
 

bin\windows\kafka-topics.bat --create --zookeeper localhost:2181 --replication-factor 1 --partitions 2 --topic favourite-colour-input
rem create output topic
bin\windows\kafka-topics.bat --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic streams-wordcount-output

rem start a kafka producer
bin\windows\kafka-console-producer.bat --broker-list localhost:9092 --topic streams-plaintext-input
bin\windows\kafka-console-producer.bat --broker-list localhost:9092 --topic word-count-input
bin\windows\kafka-console-producer.bat --broker-list localhost:9092 --topic favourite-colour-input

rem enter
kafka streams udemy
kafka data processing
kafka streams course
rem exit

rem verify the data has been written
bin\windows\kafka-console-consumer.bat --bootstrap-server localhost:9092 --topic streams-plaintext-input --from-beginning

rem start a consumer on the output topic
bin\windows\kafka-console-consumer.bat --bootstrap-server localhost:9092 ^
    --topic streams-wordcount-output ^
    --from-beginning ^
    --formatter kafka.tools.DefaultMessageFormatter ^
    --property print.key=true ^
    --property print.value=true ^
    --property key.deserializer=org.apache.kafka.common.serialization.StringDeserializer ^
    --property value.deserializer=org.apache.kafka.common.serialization.LongDeserializer
	
	
	bin\windows\kafka-console-consumer.bat --bootstrap-server localhost:9092 ^
    --topic word-count-output ^
    --from-beginning ^
    --formatter kafka.tools.DefaultMessageFormatter ^
    --property print.key=true ^
    --property print.value=true ^
    --property key.deserializer=org.apache.kafka.common.serialization.StringDeserializer ^
    --property value.deserializer=org.apache.kafka.common.serialization.LongDeserializer

	
	
	bin\windows\kafka-console-consumer.bat --bootstrap-server localhost:9092 ^
    --topic favourite-colour-output ^
    --from-beginning ^
    --formatter kafka.tools.DefaultMessageFormatter ^
    --property print.key=true ^
    --property print.value=true ^
    --property key.deserializer=org.apache.kafka.common.serialization.StringDeserializer ^
    --property value.deserializer=org.apache.kafka.common.serialization.LongDeserializer

	
	bin\windows\kafka-console-consumer.bat --bootstrap-server localhost:9092 ^
    --topic bank-balance-exactly-once ^
    --from-beginning ^
    --formatter kafka.tools.DefaultMessageFormatter ^
    --property print.key=true ^
    --property print.value=true ^
    --property key.deserializer=org.apache.kafka.common.serialization.StringDeserializer ^
    --property value.deserializer=org.apache.kafka.common.serialization.StringDeserializer
	
	bin\windows\kafka-console-consumer.bat --bootstrap-server localhost:9092 ^
    --topic bank-transactions ^
    --from-beginning
	
	
	
	
	bin\windows\kafka-console-consumer.bat --bootstrap-server localhost:9092 ^
    --topic user-purchases-enriched-left-join ^
    --from-beginning ^
    --formatter kafka.tools.DefaultMessageFormatter ^
    --property print.key=true ^
    --property print.value=true ^
    --property key.deserializer=org.apache.kafka.common.serialization.StringDeserializer ^
    --property value.deserializer=org.apache.kafka.common.serialization.StringDeserializer
	
	
	bin\windows\kafka-console-consumer.bat --bootstrap-server localhost:9092 ^
    --topic user-purchases-enriched-inner-join ^
    --from-beginning ^
    --formatter kafka.tools.DefaultMessageFormatter ^
    --property print.key=true ^
    --property print.value=true ^
    --property key.deserializer=org.apache.kafka.common.serialization.StringDeserializer ^
    --property value.deserializer=org.apache.kafka.common.serialization.StringDeserializer
	
	
	
rem start the streams application
bin\windows\kafka-run-class.bat org.apache.kafka.streams.examples.wordcount.WordCountDemo

rem verify the data has been written to the output topic!
