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

describe('CompraProducto e2e test', () => {
  const compraProductoPageUrl = '/compra-producto';
  const compraProductoPageUrlPattern = new RegExp('/compra-producto(\\?.*)?$');
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
    cy.intercept('GET', '/api/compra-productos+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/compra-productos').as('postEntityRequest');
    cy.intercept('DELETE', '/api/compra-productos/*').as('deleteEntityRequest');
  });

  it('should load CompraProductos', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('compra-producto');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('CompraProducto').should('exist');
    cy.url().should('match', compraProductoPageUrlPattern);
  });

  it('should load details CompraProducto page', function () {
    cy.visit(compraProductoPageUrl);
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        this.skip();
      }
    });
    cy.get(entityDetailsButtonSelector).first().click({ force: true });
    cy.getEntityDetailsHeading('compraProducto');
    cy.get(entityDetailsBackButtonSelector).click({ force: true });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', compraProductoPageUrlPattern);
  });

  it('should load create CompraProducto page', () => {
    cy.visit(compraProductoPageUrl);
    cy.wait('@entitiesRequest');
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('CompraProducto');
    cy.get(entityCreateSaveButtonSelector).should('exist');
    cy.get(entityCreateCancelButtonSelector).click({ force: true });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', compraProductoPageUrlPattern);
  });

  it('should load edit CompraProducto page', function () {
    cy.visit(compraProductoPageUrl);
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        this.skip();
      }
    });
    cy.get(entityEditButtonSelector).first().click({ force: true });
    cy.getEntityCreateUpdateHeading('CompraProducto');
    cy.get(entityCreateSaveButtonSelector).should('exist');
    cy.get(entityCreateCancelButtonSelector).click({ force: true });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', compraProductoPageUrlPattern);
  });

  it('should create an instance of CompraProducto', () => {
    cy.visit(compraProductoPageUrl);
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('CompraProducto');

    cy.get(`[data-cy="cantidadUds"]`).type('39900').should('have.value', '39900');

    cy.get(`[data-cy="precioUdsCompra"]`).type('61233').should('have.value', '61233');

    cy.get(`[data-cy="subTotal"]`).type('60056').should('have.value', '60056');

    cy.get(`[data-cy="iva"]`).type('62986').should('have.value', '62986');

    cy.get(`[data-cy="total"]`).type('1303').should('have.value', '1303');

    cy.get(`[data-cy="fechaVencimiento"]`).type('2021-08-31').should('have.value', '2021-08-31');

    cy.get(`[data-cy="lote"]`).type('De-engineered').should('have.value', 'De-engineered');

    cy.setFieldSelectToLastOfEntity('producto');

    cy.setFieldSelectToLastOfEntity('compra');

    cy.get(entityCreateSaveButtonSelector).click({ force: true });
    cy.scrollTo('top', { ensureScrollable: false });
    cy.get(entityCreateSaveButtonSelector).should('not.exist');
    cy.wait('@postEntityRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(201);
    });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', compraProductoPageUrlPattern);
  });

  it('should delete last instance of CompraProducto', function () {
    cy.visit(compraProductoPageUrl);
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length > 0) {
        cy.get(entityTableSelector).should('have.lengthOf', response.body.length);
        cy.get(entityDeleteButtonSelector).last().click({ force: true });
        cy.getEntityDeleteDialogHeading('compraProducto').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', compraProductoPageUrlPattern);
      } else {
        this.skip();
      }
    });
  });
});
