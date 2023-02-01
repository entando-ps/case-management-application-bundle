export default function formatDateToIt(date) {
  return new Date(date)
    .toLocaleString('it', {
      day: 'numeric',
      month: 'long',
      year: 'numeric'
    });
}
