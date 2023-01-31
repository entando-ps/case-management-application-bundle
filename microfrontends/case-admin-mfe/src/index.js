import './utils/public-path';

import React from 'react';
import ReactDOM from 'react-dom/client';
import { HashRouter as Router } from 'react-router-dom';

import styles from './index.css';
import App from './App';
import { KeycloakProvider } from './auth/Keycloak';

class CaseEntryMFE extends HTMLElement {
  #rootID = 'case-admin-mfe'
  #appInstance = null

  constructor() {
    super();

    this.attachShadow({ mode: 'open' });
  }

  static get observedAttributes() {
    return ['config'];
  }

  connectedCallback() {
    this.render()
  }

  attributeChangedCallback(_, oldValue, newValue) {
    if (newValue !== oldValue) {
      this.render();
    }
  }

  cleanTree() {
    const currentElement = this.shadowRoot.getElementById(this.#rootID);

    if (currentElement) {
      this.shadowRoot.removeChild(currentElement);
    }

    this.#appInstance?.unmount();
  }

  render() {
    const attrConf = this.getAttribute('config');
    const config = attrConf && JSON.parse(attrConf);

    if (config) {
      const shadowRootElement = document.createElement('div');

      shadowRootElement.id = this.#rootID;

      this.cleanTree();

      styles.use({ target: this.shadowRoot });

      this.#appInstance = ReactDOM.createRoot(shadowRootElement);

      this.#appInstance.render(
        <React.StrictMode>
          <KeycloakProvider>
            <Router>
              <App config={config} />
            </Router>
          </KeycloakProvider>
        </React.StrictMode>
      );

      this.shadowRoot.appendChild(shadowRootElement);
    }
  }
}

customElements.define('case-admin-mfe', CaseEntryMFE);
