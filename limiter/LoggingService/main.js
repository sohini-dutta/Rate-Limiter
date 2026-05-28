const { Kafka } = require('kafkajs');
const kafka = new Kafka({
    clientId: 'my-node-app',
    brokers: ['localhost:9092'],
});
const consumer = kafka.consumer({
    groupId: 'my-group',
});
const run = async () => {
    await consumer.connect();
    await consumer.subscribe({
        topic: 'test-topic',
        fromBeginning: true,
    });
    console.log('Kafka Consumer Started');
    await consumer.run({
        eachMessage: async ({message}) => {
            try {
                console.log((message.value.toString()));
            } catch (error) {
                console.error(error)
            }
        },
    });
};
run().catch(console.error);