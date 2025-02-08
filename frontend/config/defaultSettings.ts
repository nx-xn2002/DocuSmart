import { ProLayoutProps } from '@ant-design/pro-components';

/**
 * @name
 */
const Settings: ProLayoutProps & {
  pwa?: boolean;
  logo?: string;
} = {
  navTheme: 'light',
  // colorPrimary: '#1890ff',
  colorPrimary: '#2F54EB',
   layout: 'mix',
  //layout: 'top',
  contentWidth: 'Fluid',
  fixedHeader: false,
  fixSiderbar: true,
  colorWeak: false,
  title: 'DocuSmart',
  pwa: true,
  logo: 'logo.svg',
  iconfontUrl: '',
  token: {},
  //新增
  siderMenuType: 'sub',
  splitMenus: false,
};

export default Settings;
