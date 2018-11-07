import React from 'react';

import moment from 'moment';

export default class MessageMoment extends React.Component {
  constructor(props) {
    super(props);
    this.state = {date: moment(this.props.date).fromNow()};
  }

  componentDidMount() {
    const ONE_MINUTE = 60000;
    this.timer = setInterval(() => {
      this.tick();
    }, ONE_MINUTE);
  }

  componentWillUnmount() {
    clearInterval(this.timer);
  }

  tick() {
    this.setState({date: moment(this.props.date).fromNow()});
  }

  render() {
    return <div>{this.state.date}</div>;
  }
}
