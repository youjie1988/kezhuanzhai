package com.huaheng.mobilewms.activity.printer;

import com.gprinter.command.EscCommand;
import com.gprinter.command.LabelCommand;

import java.util.Vector;

/**
 * Created by Administrator on 2018/4/16.
 */

public class PrintContent {

      /**
       * 标签打印物料号
       * @return
       */
      public static Vector<Byte> printMaterial(String code, String name, String spec, String qty) {
            if(name == null) {
                  name = "";
            }
            if(spec == null) {
                  spec = "";
            }
            if(qty == null) {
                  qty = "";
            }
            LabelCommand tsc = new LabelCommand();
            // 设置标签尺寸宽高，按照实际尺寸设置 单位mm
            tsc.addSize(58, 40);
            // 设置标签间隙，按照实际尺寸设置，如果为无间隙纸则设置为0 单位mm
            tsc.addGap(5);
            // 设置打印方向
            tsc.addDirection(LabelCommand.DIRECTION.FORWARD, LabelCommand.MIRROR.NORMAL);
            // 开启带Response的打印，用于连续打印
            tsc.addQueryPrinterStatus(LabelCommand.RESPONSE_MODE.ON);
            // 设置原点坐标
            tsc.addReference(0, 0);
            //设置浓度
            tsc.addDensity(LabelCommand.DENSITY.DNESITY4);
            // 撕纸模式开启
            tsc.addTear(EscCommand.ENABLE.ON);
            // 清除打印缓冲区
            tsc.addCls();
            // 绘制简体中文
            tsc.addText(20, 20, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_2, LabelCommand.FONTMUL.MUL_2,
                    "编码:" + code);
            tsc.addText(20, 80, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_2, LabelCommand.FONTMUL.MUL_2,
                    "名称:" + name);
            tsc.addText(20, 140, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_2, LabelCommand.FONTMUL.MUL_2,
                    "规格:" + spec);
            tsc.addText(20, 200, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_2, LabelCommand.FONTMUL.MUL_2,
                    "数量:" + qty);
            tsc.addCutter(EscCommand.ENABLE.ON);
            //绘制二维码
            String qtCode = code + "/" + name + "/" + spec + "/" + qty;
            tsc.addQRCode(280,200, LabelCommand.EEC.LEVEL_L, 4, LabelCommand.ROTATION.ROTATION_0, qtCode);;
            // 打印标签
            tsc.addPrint(1, 1);
            // 打印标签后 蜂鸣器响
            tsc.addSound(2, 100);
            //开启钱箱
            tsc.addCashdrwer(LabelCommand.FOOT.F5, 255, 255);
            Vector<Byte> datas = tsc.getCommand();
            // 发送数据
            return  datas;
      }

      /**
       * 标签打印单据
       * @return
       */
      public static Vector<Byte> printList(String receipt, String type, String refer, String createBy) {
            if(type == null) {
                  type = "";
            }
            if(refer == null) {
                  refer = "";
            }
            if(createBy == null) {
                  createBy = "";
            }
            LabelCommand tsc = new LabelCommand();
            // 设置标签尺寸宽高，按照实际尺寸设置 单位mm
            tsc.addSize(58, 40);
            // 设置标签间隙，按照实际尺寸设置，如果为无间隙纸则设置为0 单位mm
            tsc.addGap(5);
            // 设置打印方向
            tsc.addDirection(LabelCommand.DIRECTION.FORWARD, LabelCommand.MIRROR.NORMAL);
            // 开启带Response的打印，用于连续打印
            tsc.addQueryPrinterStatus(LabelCommand.RESPONSE_MODE.ON);
            // 设置原点坐标
            tsc.addReference(0, 0);
            //设置浓度
            tsc.addDensity(LabelCommand.DENSITY.DNESITY4);
            // 撕纸模式开启
            tsc.addTear(EscCommand.ENABLE.ON);
            // 清除打印缓冲区
            tsc.addCls();
            // 绘制简体中文
            tsc.addText(20, 20, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_2, LabelCommand.FONTMUL.MUL_2,
                    "入库单:" + receipt);
            tsc.addText(20, 80, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_2, LabelCommand.FONTMUL.MUL_2,
                    "类型:" + type);
            tsc.addText(20, 140, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_2, LabelCommand.FONTMUL.MUL_2,
                    "关联单:" + refer);
            tsc.addText(20, 200, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_2, LabelCommand.FONTMUL.MUL_2,
                    "创建人:" + createBy);
            tsc.addCutter(EscCommand.ENABLE.ON);
            //绘制二维码
            String qtCode = refer;
            tsc.addQRCode(280,200, LabelCommand.EEC.LEVEL_L, 5, LabelCommand.ROTATION.ROTATION_0, qtCode);;
            // 打印标签
            tsc.addPrint(1, 1);
            // 打印标签后 蜂鸣器响
            tsc.addSound(2, 100);
            //开启钱箱
            tsc.addCashdrwer(LabelCommand.FOOT.F5, 255, 255);
            Vector<Byte> datas = tsc.getCommand();
            // 发送数据
            return  datas;
      }
}
