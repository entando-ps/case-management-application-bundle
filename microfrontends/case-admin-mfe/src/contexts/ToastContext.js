import { createContext, useContext, useState } from 'react';
import { Toast, ToastContainer } from 'react-bootstrap';

export const ToastContext = createContext(null);

export function ToastProvider({ children }) {
  const [toast, setToast] = useState({
    open: false,
    variant: 'light',
    message: '',
  });

  const showToast = (message, variant) => {
    setToast({
      open: true,
      message,
      variant,
    });
  };

  return (
    <ToastContext.Provider value={{ showToast }}>
      {children}
      <ToastContainer position="top-end" className="p-3 position-fixed">
        <Toast
          bg={toast.variant}
          onClose={() => setToast({ open: false })}
          show={toast.open}
          delay={5000} 
          autohide
        >
          <Toast.Body className="text-white">{toast.message}</Toast.Body>
        </Toast>
      </ToastContainer>
    </ToastContext.Provider>
  )
}

export const useToast = () => {
  return useContext(ToastContext);
};
