import type { RequestOptions } from '@@/plugin-request/request';
import type { RequestConfig } from '@umijs/max';

// 错误处理方案： 错误类型
enum ErrorShowType {
  SILENT = 0,
  WARN_MESSAGE = 1,
  ERROR_MESSAGE = 2,
  NOTIFICATION = 3,
  REDIRECT = 9,
}

// 与后端约定的响应数据格式
interface ResponseStructure {
  success: boolean;
  data: any;
  errorCode?: number;
  errorMessage?: string;
  showType?: ErrorShowType;
}

export const requestConfig: RequestConfig = {
  baseURL: 'http://localhost:8101/api',
  withCredentials: true,

  // 错误处理： umi@3 的错误处理方案。
  errorConfig: {
    errorThrower: (res) => {
      const { success, data, errorCode, errorMessage, showType } =
        res as unknown as ResponseStructure;
      if (!success) {
        const error: any = new Error(errorMessage);
        error.name = 'BizError';
        error.info = { errorCode, errorMessage, showType, data };
        throw error;
      }
    },
  },

  // 请求拦截器
  requestInterceptors: [
    (config: RequestOptions) => {
      const url = config?.url?.concat('?token=123'); // 例如传递token
      return { ...config, url };
    },
  ],

  // 响应拦截器
  responseInterceptors: [
    (response) => {
      // 检查响应的类型
      const contentType = response.headers['content-type'];

      // 如果响应类型是二进制流（.docx文件）
      if (contentType && contentType.includes('application/vnd.openxmlformats-officedocument.wordprocessingml.document')) {
        return response; // 对于二进制文件，直接返回响应，避免触发错误处理
      }

      // 如果是 JSON 响应，继续按原逻辑处理
      const { data } = response as unknown as ResponseStructure;
      if (data.code !== 0) {
        throw new Error(data.message);
      }

      return response;
    },
  ],
};
