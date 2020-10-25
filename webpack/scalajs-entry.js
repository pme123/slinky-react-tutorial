if (process.env.NODE_ENV === "production") {
    const opt = require("./slinky-react-tutorial-opt.js");
    opt.main();
    module.exports = opt;
} else {
    var exports = window;
    exports.require = require("./slinky-react-tutorial-fastopt-entrypoint.js").require;
    window.global = window;

    const fastOpt = require("./slinky-react-tutorial-fastopt.js");
    fastOpt.main()
    module.exports = fastOpt;

    if (module.hot) {
        module.hot.accept();
    }
}
