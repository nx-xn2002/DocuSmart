// @ts-ignore
/* eslint-disable */
import { request } from '@umijs/max';

/** 此处后端没有提供注释 POST /generate/doc */
export async function generateDocument(
  params: API.generateDocumentParams,
  body: {},
  files: FormData, // 文件通过FormData上传
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseString>('/generate/doc', {
    method: 'POST',
    params: {
      ...params,
    },
    data: files, // 直接传递FormData对象
    requestType: 'form', // 确保请求类型为form
    ...(options || {}),
  });
}
