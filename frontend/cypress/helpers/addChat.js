import openChatModal from './openChatModal';
import fillChatModal from './fillChatModal';
import sendMessage from './addMessage';
import checkMessage from './checkMessage';

export default function addChat(amountOfPeers, amountOfMessages) {
  openChatModal();

  const chatName = 'TestChat - ' + Math.random();
  const messages = new Array(amountOfPeers).fill(amountOfMessages).map(element => Math.random());

  fillChatModal(chatName, amountOfPeers);

  sendMessage(messages);

  checkMessage(chatName, messages);
}
