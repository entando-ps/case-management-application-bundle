export default function deriveCaseState({ state, processData }) {
  if (state === 'RUNNING') {
    return 'Aperta';
  }

  if (state === 'COMPLETED') {
    return processData?.approved ? 'Approvata' : 'Rifiutata';
  }

  return null;
}
