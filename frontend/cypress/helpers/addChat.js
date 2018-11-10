import openChatModal from './helperOpenChatModal';
import fillChatModal from './helperFillChatModal';
import sendMessage from './helperMessage';

export default function addChat(amountOfPeers, amountOfMessages) {
  openChatModal();

  const chatName = 'TestChat - ' + Math.random();
  fillChatModal(chatName, amountOfPeers);

  sendMessage(amountOfMessages);
}
