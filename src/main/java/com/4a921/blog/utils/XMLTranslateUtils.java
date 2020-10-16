package com.knowswift.myspringboot.utils;

import com.knowswift.myspringboot.bean.wechat.vo.CiphertextWechatMessage;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

public class XMLTranslateUtils {

    /**
     * java对象转成xml文件
     *
     * @param obj      java对象
     * @param load     类对象
     * @param encoding 编码格式
     * @return
     * @throws JAXBException
     */
    public static String beanToXml(Object obj, Class<?> load, String encoding) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(load);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, false);
        marshaller.setProperty(Marshaller.JAXB_ENCODING, encoding);
        StringWriter writer = new StringWriter();
        marshaller.marshal(obj, writer);
        return writer.toString();
    }

    /**
     * xml文件转换成java对象
     *
     * @param xmlPath xml文件路径
     * @param load    java对象.class
     * @param <T>
     * @return
     * @throws JAXBException
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    public static <T> T xmlToBean(String xmlPath, Class<T> load) throws JAXBException, IOException {
        JAXBContext context = JAXBContext.newInstance(load);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        return (T) unmarshaller.unmarshal(new StringReader(xmlPath));
    }

    /**
     * JavaBean转换成xml,默认编码UTF-8
     *
     * @param obj java对象
     * @return
     */
    public static String convertToXml(Object obj) {
        return convertToXml(obj, "UTF-8");
    }

    /**
     * JavaBean转换成xml
     *
     * @param obj      java对象
     * @param encoding 编码格式
     * @return
     */
    public static String convertToXml(Object obj, String encoding) {
        String result = null;
        try {
            JAXBContext context = JAXBContext.newInstance(obj.getClass());
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.setProperty(Marshaller.JAXB_ENCODING, encoding);
            StringWriter writer = new StringWriter();
            marshaller.marshal(obj, writer);
            result = writer.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * JavaBean转换成xml去除xml声明部分
     *
     * @param obj      java对象
     * @param encoding 编码
     * @return
     */
    public static String convertToXmlIgnoreXmlHead(Object obj, String encoding) {
        String result = null;
        try {
            JAXBContext context = JAXBContext.newInstance(obj.getClass());
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.setProperty(Marshaller.JAXB_ENCODING, encoding);
            marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
            StringWriter writer = new StringWriter();
            marshaller.marshal(obj, writer);
            result = writer.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * xml转换成JavaBean
     *
     * @param xml xml字符串
     * @param c   java字节码
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T converToJavaBean(String xml, Class<T> c) {
        T t = null;
        try {
            JAXBContext context = JAXBContext.newInstance(c);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            t = (T) unmarshaller.unmarshal(new StringReader(xml));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return t;
    }

    public static void main(String[] args) {
        String xml = "<xml>\n" +
                "    <ToUserName><![CDATA[gh_65ebb718db6e]]></ToUserName>\n" +
                "    <Encrypt><![CDATA[F1B0xz4x8GUMDlExsfIQtVn/RTUYiaJUyIOkKbD+k1WfryMlPrLtU2NUnnOl00f09KhUVF7rvKRDNvRB+wUSZDmBKrVLWiQDtH+QrJ0m1Vn0tQ3GcqFgWGdp1jCEY/vc6iV2eGtdH5zSXpGIvSypYyVEhITlpQHIxgEmbettXlvceD2o+OCKQM7B4L2MBKsB4FquzxIq7JPyMPQj9xJ4sfoQ9Ay4fGhpav62VTbe5+26pVIqpMS/uu+XlLR8tzzaHO0VAqOvzYMtXzoBvUOSLMQmZbV/UdhkQu1cgphS3sZ7fWjilvY8QMsyTJWuU7Wmb0MwQf0VrLO5wODKrpx+A7ksuaf3gZ0vpwJO++gDbuMO7KBBU7Qzls2lj1kfgUlzb8IX5uNzFWghcLC2sjE6g3i/p/CMaVkEqiYPPD9iLzw=]]></Encrypt>\n" +
                "</xml>";
        System.out.println(converToJavaBean(xml, CiphertextWechatMessage.class));
    }

}
