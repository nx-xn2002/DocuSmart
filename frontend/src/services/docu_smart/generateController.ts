// @ts-ignore
/* eslint-disable */
import { request } from '@umijs/max';

/** 此处后端没有提供注释 POST /generate/doc */
export async function generateDocument(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.generateDocumentParams,
  options?: { [key: string]: any },
  files: File[],
) {
  return request<API.BaseResponseString>('/generate/doc', {
    method: 'POST',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}
