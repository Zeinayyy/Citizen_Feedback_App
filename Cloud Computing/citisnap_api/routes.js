const { indexHandler } = require("../handlers");

const routes = [
    {
        path : "/",
        method : "get",
        handler : indexHandler
    }
]

module.exports = routes