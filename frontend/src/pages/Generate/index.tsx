import { generateDocument } from '@/services/docu_smart/generateController';
import { listTemplateByPage } from '@/services/docu_smart/templateController';
import { UploadOutlined } from '@ant-design/icons';
import '@umijs/max';
import {
  Button,
  Card,
  Col,
  Form,
  Grid,
  Image,
  Input,
  message,
  Radio,
  RadioChangeEvent,
  Row,
  Space,
  Typography,
  Upload,
} from 'antd';
import React, { useEffect, useState } from 'react';

const { useBreakpoint } = Grid;
const { Text } = Typography;
const { TextArea } = Input;
const { Meta } = Card;

const Generate: React.FC = () => {
  const [submitting, setSubmitting] = useState<boolean>();
  const [selectedTemplate, setSelectedTemplate] = useState<API.Template>();
  const [tip, setTip] = useState<string>('请先选择模版');
  const [templateList, setTemplateList] = useState<API.Template[]>([]);
  const [fileList, setFileList] = useState<any[]>([]);
  const screens = useBreakpoint(); // 获取当前的屏幕尺寸

  const getTemplate = async () => {
    try {
      const res = await listTemplateByPage({
        templateQueryRequest: {
          current: 1,
          pageSize: 50,
        },
      });
      if (res?.data) {
        setTemplateList(res?.data?.records || []);
      }
    } catch (error: any) {
      message.error('获取模版失败，' + error.message);
    }
  };

  useEffect(() => {
    getTemplate();
  }, []);

  const onChange = (e: RadioChangeEvent) => {
    setSelectedTemplate(e.target.value);
    setTip(e.target.value.description);
  };

  const onFinish = async (values: any) => {
    if (submitting) return;
    setSubmitting(true);

    // 获取上传的文件列表
    const files = values.fileList ? values.fileList.map((file: any) => file.originFileObj) : [];
    console.log('提交的数据：', {
      templateId: selectedTemplate?.id !== undefined ? selectedTemplate?.id : -1,
      content: values.content,
      fileList: files, // 输出文件列表
    });

    const param = {
      templateId: selectedTemplate?.id !== undefined ? selectedTemplate?.id : -1,
      content: values.content,
      fileList: files, // 将文件列表添加到参数中
    };

    try {
      console.log('开始调用生成文档接口...');

      // 调用生成文档 API
      const res = await generateDocument(param, {}, files);
      const base64Str = res.data; // 假设返回值包含在data字段内
      const byteCharacters = atob(base64Str ? base64Str : ''); // 解码Base64字符串
      const byteArrays = [];

      for (let offset = 0; offset < byteCharacters.length; offset += 1024) {
        const slice = byteCharacters.slice(offset, offset + 1024);
        const byteNumbers = new Array(slice.length);
        for (let i = 0; i < slice.length; i++) {
          byteNumbers[i] = slice.charCodeAt(i);
        }
        const byteArray = new Uint8Array(byteNumbers);
        byteArrays.push(byteArray);
      }
      const blob = new Blob(byteArrays, {
        type: 'application/vnd.openxmlformats-officedocument.wordprocessingml.document',
      });
      const link = document.createElement('a');
      link.href = URL.createObjectURL(blob);
      link.download = 'GeneratedDocument.docx'; // 设置下载的文件名
      link.click(); // 点击下载链接
      setSubmitting(false);
    } catch (e: any) {
      // 细化错误日志
      console.error('文件生成失败：', e);
      if (e.response) {
        console.error('接口响应错误：', e.response);
      } else if (e.message) {
        console.error('错误信息：', e.message);
      }
      setSubmitting(false);
    }
  };

  return (
    <div className="add-chart-async">
      <Card title="公文生成" style={{ width: '90%', maxWidth: '90%' }}>
        <Form
          name="add-chart"
          labelAlign="left"
          labelCol={{ span: 4 }}
          wrapperCol={{ span: 16 }}
          onFinish={onFinish}
          initialValues={{
            templateId: -1,
          }}
        >
          <Form.Item name="fileName" label="文件名（选填）">
            <Input showCount maxLength={40} placeholder="请输入目标文件名" />
          </Form.Item>
          <Form.Item name="templateId" label="公文模板" wrapperCol={{ span: 12 }}>
            <Radio.Group onChange={onChange} value={selectedTemplate}>
              <Row gutter={[{ xs: 96, sm: 120, md: 144, lg: 192 }, 24]}>
                {templateList.map((template) => (
                  <Col key={template.id} xs={12} sm={8} lg={6} xl={4}>
                    <Radio value={template} required={true}>
                      <Card
                        hoverable
                        style={{ width: '100%' }}
                        cover={
                          <Image
                            src={`data:image/png;base64,${template.preview}`}
                            alt={template.templateName}
                            style={{ height: 60, objectFit: 'cover' }}
                          />
                        }
                      >
                        <Meta title={template.templateName} />
                      </Card>
                    </Radio>
                  </Col>
                ))}
              </Row>
            </Radio.Group>
          </Form.Item>
          <Form.Item
            name="content"
            label={<Text>{tip}</Text>}
            labelAlign="left"
            labelCol={{ span: 24 }}
            wrapperCol={{ span: 20 }}
            style={{ marginBottom: 4 }}
            rules={[{ required: true, message: '描述信息是必填项！' }]}
          >
            <TextArea showCount maxLength={800} placeholder="描述信息" />
          </Form.Item>
          <Form.Item name="file" label="参考文档" extra="支持文件类型.docx">
            <Upload
              name="fileList"
              multiple
              maxCount={5}
              onChange={({ fileList }) => {
                // 通过 onChange 更新 fileList
                setFileList(fileList);
              }}
              fileList={fileList} // 这里绑定 fileList
            >
              <Button icon={<UploadOutlined />}>上传文件</Button>
            </Upload>
          </Form.Item>

          <Form.Item wrapperCol={{ span: 20, offset: 4 }}>
            <Space direction={screens.sm ? 'horizontal' : 'vertical'}>
              <Button type="primary" htmlType="submit" loading={submitting}>
                {submitting ? '生成中...' : '开始生成'}
              </Button>
              <Button
                htmlType="reset"
                onClick={() => {
                  setTip('请先选择模版');
                  setSelectedTemplate(undefined);
                }}
                disabled={submitting} // 防止在提交时点击重置
              >
                重新输入
              </Button>
            </Space>
          </Form.Item>
        </Form>
      </Card>
    </div>
  );
};

export default Generate;
