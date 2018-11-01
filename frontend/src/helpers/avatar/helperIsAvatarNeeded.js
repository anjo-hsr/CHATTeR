export default function isAvatarNeeded(array, index) {
  if (index === 0) {
    return true;
  }
  return array[index - 1].isMe !== array[index].isMe;
}
