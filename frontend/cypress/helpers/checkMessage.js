import openChatModal from './openChatModal';
import fillChatModal from './fillChatModal';
import sendMessage from './addMessage';

export default function addChat(amountOfPeers, amountOfMessages) {
  openChatModal();

  const chatName = 'TestChat - ' + Math.random();
  fillChatModal(chatName, amountOfPeers);

  sendMessage(amountOfMessages);
}
