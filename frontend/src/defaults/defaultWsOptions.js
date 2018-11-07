let defaultWsOptions = {
  protocol: 'ws',
  server: window.location.hostname,
  port: 8000
};

defaultWsOptions.baseString = `${defaultWsOptions.protocol}://${defaultWsOptions.server}:${defaultWsOptions.port}/chat`;

export default Object.freeze(Object.assign({}, defaultWsOptions));
