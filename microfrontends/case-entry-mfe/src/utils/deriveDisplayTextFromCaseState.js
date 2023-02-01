export default function deriveDisplayTextFromCaseState({ state, metadata }) {
  if (state === 'RUNNING') {
    return 'Aperta';
  }

  if (state === 'COMPLETED') {
    return metadata?.processData?.approved ? 'Approvata' : 'Rifiutata';
  }

  return null;
}
