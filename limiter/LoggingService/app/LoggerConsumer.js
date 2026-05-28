if (typeof globalThis.crypto === 'undefined') {
  globalThis.crypto = require('crypto').webcrypto;
} else if (typeof globalThis.crypto.getRandomValues === 'undefined') {
  globalThis.crypto.getRandomValues = require('crypto').webcrypto.getRandomValues;
}

const { Kafka } = require('kafkajs');
const connectDB = require('../DB/mongoInstance')
const LogEntrySchema = require('../DB/schema')

async function connectToMongo(){
    try {
        await connectDB();
        console.log("DB connected")
    } catch (error) {
        console.log(error)
    }
}

const kafka = new Kafka({
    clientId: process.env.clientId,
    brokers: [process.env.brokerPort],
});
const consumer = kafka.consumer({
    groupId: process.env.groupId,
});

connectToMongo();

const run = async () => {
    await consumer.connect();
    await consumer.subscribe({
        topic: 'test-topic',
        fromBeginning: true,
    });
    console.log('Kafka Consumer Started');
    await consumer.run({
        eachMessage: async ({message , partition , topic}) => {
            try {
                const jsonObj = message.value.toString();
                var payloadObj = JSON.parse(jsonObj);
                payloadObj.kafka_partition = partition;
                payloadObj.kafka_topic = topic;
                const newLogEntry = new LogEntrySchema(payloadObj);
                await newLogEntry.save();
                console.log("Entry Saved")
            } catch (error) {
                console.error(error)
            }
        },
    });
};
run().catch(console.error);