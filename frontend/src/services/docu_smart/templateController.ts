// @ts-ignore
/* eslint-disable */
import { request } from '@umijs/max';

/** 此处后端没有提供注释 POST /template/add */
export async function addTemplate(body: API.TemplateAddRequest, options?: { [key: string]: any }) {
  return request<API.BaseResponseLong>('/template/add', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 此处后端没有提供注释 POST /template/delete */
export async function deleteTemplate(body: API.DeleteRequest, options?: { [key: string]: any }) {
  return request<API.BaseResponseBoolean>('/template/delete', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 此处后端没有提供注释 GET /template/get */
export async function getTemplateById(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getTemplateByIdParams,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseTemplate>('/template/get', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** 此处后端没有提供注释 GET /template/list */
export async function listTemplate(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.listTemplateParams,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseListTemplate>('/template/list', {
    method: 'GET',
    params: {
      ...params,
      templateQueryRequest: undefined,
      ...params['templateQueryRequest'],
    },
    ...(options || {}),
  });
}

/** 此处后端没有提供注释 GET /template/list/page */
export async function listTemplateByPage(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.listTemplateByPageParams,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponsePageTemplate>('/template/list/page', {
    method: 'GET',
    params: {
      ...params,
      templateQueryRequest: undefined,
      ...params['templateQueryRequest'],
    },
    ...(options || {}),
  });
}

/** 此处后端没有提供注释 POST /template/update */
export async function updateTemplate(
  body: API.TemplateUpdateRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseBoolean>('/template/update', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}
