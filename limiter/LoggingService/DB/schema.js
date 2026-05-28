const mongoose = require('mongoose')


const schema = new mongoose.Schema({
    userID : {
        type: String,
        required: [true, 'User ID is required']
    },
    message : {
        type: String,
    },
    endpoint : {
        type: String,
    },
    timeStamp : {
        type: Date,
    },
    kafka_partition : {
        type: String,
    },
    kafka_topic : {
        type: String,
    },

})

module.exports = mongoose.model("LogEntrySchema",schema)