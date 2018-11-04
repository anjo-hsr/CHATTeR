let defaultWsOptions = {
  protocol: 'ws',
  server: 'localhost',
  port: 8000
};

defaultWsOptions.baseString = `${defaultWsOptions.protocol}://${defaultWsOptions.server}:${defaultWsOptions.port}/chat`;

export default Object.freeze(Object.assign({}, defaultWsOptions));
