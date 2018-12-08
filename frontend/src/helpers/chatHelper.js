export default function isOtherAuthorThanBefore(array, index) {
  if (index === 0) {
    return true;
  }
  return array[index - 1].author !== array[index].author;
}
