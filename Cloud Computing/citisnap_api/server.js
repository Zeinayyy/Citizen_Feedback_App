require("dotenv").config();
const Hapi = require("@hapi/hapi");
const routes = require("./routes");

const init = async () => {
    const server = Hapi.server({
        port: process.env.PORT || 5000,
        host: process.env.NODE_ENV !== 'production' ? 'localhost' : process.env.HOST,
        routes: {
            cors: {
              origin: ['*'],
            },
          },
    });
    server.route(routes)

    await server.start();
    console.log('Server running on %s', server.info.uri);
};

init();