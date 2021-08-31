import { entityItemSelector } from '../../support/commands';
import {
  entityTableSelector,
  entityDetailsButtonSelector,
  entityDetailsBackButtonSelector,
  entityCreateButtonSelector,
  entityCreateSaveButtonSelector,
  entityCreateCancelButtonSelector,
  entityEditButtonSelector,
  entityDeleteButtonSelector,
  entityConfirmDeleteButtonSelector,
} from '../../support/entity';

describe('Cliente e2e test', () => {
  const clientePageUrl = '/cliente';
  const clientePageUrlPattern = new RegExp('/cliente(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/clientes+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/clientes').as('postEntityRequest');
    cy.intercept('DELETE', '/api/clientes/*').as('deleteEntityRequest');
  });

  it('should load Clientes', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('cliente');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Cliente').should('exist');
    cy.url().should('match', clientePageUrlPattern);
  });

  it('should load details Cliente page', function () {
    cy.visit(clientePageUrl);
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        this.skip();
      }
    });
    cy.get(entityDetailsButtonSelector).first().click({ force: true });
    cy.getEntityDetailsHeading('cliente');
    cy.get(entityDetailsBackButtonSelector).click({ force: true });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', clientePageUrlPattern);
  });

  it('should load create Cliente page', () => {
    cy.visit(clientePageUrl);
    cy.wait('@entitiesRequest');
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('Cliente');
    cy.get(entityCreateSaveButtonSelector).should('exist');
    cy.get(entityCreateCancelButtonSelector).click({ force: true });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', clientePageUrlPattern);
  });

  it('should load edit Cliente page', function () {
    cy.visit(clientePageUrl);
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        this.skip();
      }
    });
    cy.get(entityEditButtonSelector).first().click({ force: true });
    cy.getEntityCreateUpdateHeading('Cliente');
    cy.get(entityCreateSaveButtonSelector).should('exist');
    cy.get(entityCreateCancelButtonSelector).click({ force: true });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', clientePageUrlPattern);
  });

  it('should create an instance of Cliente', () => {
    cy.visit(clientePageUrl);
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('Cliente');

    cy.get(`[data-cy="primerNombre"]`).type('purple Plastic').should('have.value', 'purple Plastic');

    cy.get(`[data-cy="segundoNombre"]`).type('generating Solutions').should('have.value', 'generating Solutions');

    cy.get(`[data-cy="primerApellido"]`).type('synergies generate').should('have.value', 'synergies generate');

    cy.get(`[data-cy="segundoApellido"]`).type('Marino Harbor Guarani').should('have.value', 'Marino Harbor Guarani');

    cy.get(`[data-cy="primerTelefono"]`).type('Architect orchestrate generating').should('have.value', 'Architect orchestrate generating');

    cy.get(`[data-cy="segundoTelefono"]`).type('Future Toys').should('have.value', 'Future Toys');

    cy.setFieldSelectToLastOfEntity('municipio');

    cy.setFieldSelectToLastOfEntity('genero');

    cy.get(entityCreateSaveButtonSelector).click({ force: true });
    cy.scrollTo('top', { ensureScrollable: false });
    cy.get(entityCreateSaveButtonSelector).should('not.exist');
    cy.wait('@postEntityRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(201);
    });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', clientePageUrlPattern);
  });

  it('should delete last instance of Cliente', function () {
    cy.visit(clientePageUrl);
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length > 0) {
        cy.get(entityTableSelector).should('have.lengthOf', response.body.length);
        cy.get(entityDeleteButtonSelector).last().click({ force: true });
        cy.getEntityDeleteDialogHeading('cliente').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', clientePageUrlPattern);
      } else {
        this.skip();
      }
    });
  });
});
