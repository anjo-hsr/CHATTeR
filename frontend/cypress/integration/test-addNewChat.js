import setViewport from '../helpers/setViewport';
import addChat from '../helpers/addChat';
import deleteChat from '../helpers/deleteChat';
import deleteAllChats from '../helpers/deleteAllChats';

describe('Exercise tests', function() {
  before(() => {});

  beforeEach(() => {
    setViewport('fullHD');
  });

  it('Create new chat with one member', function() {
    addChat(1, 2);
  });

  it('Create new groupChat with two members', function() {
    addChat(2, 3);
  });

  afterEach(() => {
    deleteChat();
  });

  after(() => {
    deleteAllChats();
  });
});
