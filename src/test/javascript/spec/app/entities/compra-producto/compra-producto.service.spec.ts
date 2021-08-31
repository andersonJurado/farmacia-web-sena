/* tslint:disable max-line-length */
import axios from 'axios';
import sinon from 'sinon';
import dayjs from 'dayjs';

import { DATE_FORMAT } from '@/shared/date/filters';
import CompraProductoService from '@/entities/compra-producto/compra-producto.service';
import { CompraProducto } from '@/shared/model/compra-producto.model';

const error = {
  response: {
    status: null,
    data: {
      type: null,
    },
  },
};

const axiosStub = {
  get: sinon.stub(axios, 'get'),
  post: sinon.stub(axios, 'post'),
  put: sinon.stub(axios, 'put'),
  patch: sinon.stub(axios, 'patch'),
  delete: sinon.stub(axios, 'delete'),
};

describe('Service Tests', () => {
  describe('CompraProducto Service', () => {
    let service: CompraProductoService;
    let elemDefault;
    let currentDate: Date;

    beforeEach(() => {
      service = new CompraProductoService();
      currentDate = new Date();
      elemDefault = new CompraProducto(123, 0, 0, 0, 0, 0, currentDate, 'AAAAAAA');
    });

    describe('Service methods', () => {
      it('should find an element', async () => {
        const returnedFromService = Object.assign(
          {
            fechaVencimiento: dayjs(currentDate).format(DATE_FORMAT),
          },
          elemDefault
        );
        axiosStub.get.resolves({ data: returnedFromService });

        return service.find(123).then(res => {
          expect(res).toMatchObject(elemDefault);
        });
      });

      it('should not find an element', async () => {
        axiosStub.get.rejects(error);
        return service
          .find(123)
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should create a CompraProducto', async () => {
        const returnedFromService = Object.assign(
          {
            id: 123,
            fechaVencimiento: dayjs(currentDate).format(DATE_FORMAT),
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            fechaVencimiento: currentDate,
          },
          returnedFromService
        );

        axiosStub.post.resolves({ data: returnedFromService });
        return service.create({}).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not create a CompraProducto', async () => {
        axiosStub.post.rejects(error);

        return service
          .create({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should update a CompraProducto', async () => {
        const returnedFromService = Object.assign(
          {
            cantidadUds: 1,
            precioUdsCompra: 1,
            subTotal: 1,
            iva: 1,
            total: 1,
            fechaVencimiento: dayjs(currentDate).format(DATE_FORMAT),
            lote: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            fechaVencimiento: currentDate,
          },
          returnedFromService
        );
        axiosStub.put.resolves({ data: returnedFromService });

        return service.update(expected).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not update a CompraProducto', async () => {
        axiosStub.put.rejects(error);

        return service
          .update({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should partial update a CompraProducto', async () => {
        const patchObject = Object.assign(
          {
            cantidadUds: 1,
            precioUdsCompra: 1,
            lote: 'BBBBBB',
          },
          new CompraProducto()
        );
        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            fechaVencimiento: currentDate,
          },
          returnedFromService
        );
        axiosStub.patch.resolves({ data: returnedFromService });

        return service.partialUpdate(patchObject).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not partial update a CompraProducto', async () => {
        axiosStub.patch.rejects(error);

        return service
          .partialUpdate({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should return a list of CompraProducto', async () => {
        const returnedFromService = Object.assign(
          {
            cantidadUds: 1,
            precioUdsCompra: 1,
            subTotal: 1,
            iva: 1,
            total: 1,
            fechaVencimiento: dayjs(currentDate).format(DATE_FORMAT),
            lote: 'BBBBBB',
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            fechaVencimiento: currentDate,
          },
          returnedFromService
        );
        axiosStub.get.resolves([returnedFromService]);
        return service.retrieve({ sort: {}, page: 0, size: 10 }).then(res => {
          expect(res).toContainEqual(expected);
        });
      });

      it('should not return a list of CompraProducto', async () => {
        axiosStub.get.rejects(error);

        return service
          .retrieve()
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should delete a CompraProducto', async () => {
        axiosStub.delete.resolves({ ok: true });
        return service.delete(123).then(res => {
          expect(res.ok).toBeTruthy();
        });
      });

      it('should not delete a CompraProducto', async () => {
        axiosStub.delete.rejects(error);

        return service
          .delete(123)
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });
    });
  });
});
