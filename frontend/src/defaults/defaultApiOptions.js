let defaultApiOptions = {
  protocol: 'http',
  server: 'localhost',
  port: 4000
};

defaultApiOptions.baseString = `${defaultApiOptions.protocol}://${defaultApiOptions.server}:${defaultApiOptions.port}`;

export default Object.freeze(Object.assign({}, defaultApiOptions));
