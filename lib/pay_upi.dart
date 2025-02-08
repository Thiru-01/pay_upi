import 'package:pay_upi/model/UpiAppData.dart';
import 'package:pay_upi/pay_upi_platform_interface.dart';

import 'model/UpiTransaction.dart';

class PayUpi {
  Future<List<UpiAppData>> getInstalledUpiApplications() async {
    List<UpiAppData> listOfUpiApp = [];
    final List<Object?> nativeObject = await PayUpiPlatform.instance
        .getInstalledUpiApplications(_listOfUpiApps);
    for (Object? upiApp in nativeObject) {
      if (upiApp != null && upiApp is Map) {
        listOfUpiApp.add(UpiAppData.ofNative(upiApp));
      }
    }
    return listOfUpiApp;
  }

  Future<void> initiateTransaction(
      double amount, String appPackageName, Uri qrRequest) async {
    final request = UpiTransactionRequest(
        amount: amount, appPackageName: appPackageName, qrRequest: qrRequest);
    await PayUpiPlatform.instance.initiateTransaction(request);
  }

  Stream<dynamic> get intentStream => PayUpiPlatform.instance.intentStream();
}

const List<String> _listOfUpiApps = [
  "com.SIBMobile",
  "com.YesBank",
  "com.atomyes",
  "com.aubank.aupay.bhimupi",
  "com.bankofbaroda.upi",
  "com.canarabank.mobility",
  "com.citrus.citruspay",
  "com.csam.icici.bank.imobile",
  "com.cub.plus.gui",
  "com.cub.wallet.gui",
  "com.dbs.in.digitalbank",
  "com.dreamplug.androidapp",
  "com.enstage.wibmo.hdfc",
  "com.equitasbank.upi",
  "com.euronet.iobupi",
  "com.fampay.in",
  "com.finacus.jetpay",
  "com.finacus.tranzapp",
  "com.finopaytech.bpayfino",
  "com.fisglobal.bandhanupi.app",
  "com.fisglobal.syndicateupi.app",
  "com.freecharge.android",
  "com.fss.idfcpsp",
  "com.fss.ippbpsp",
  "com.fss.jnkpsp",
  "com.fss.pnbpsp",
  "com.fss.unbipsp",
  "com.fss.vijayapsp",
  "com.google.android.apps.nbu.paisa.user",
  "com.idbibank.paywiz",
  "com.infra.boiupi",
  "com.infrasoft.uboi",
  "com.infrasofttech.centralbankupi",
  "com.infrasofttech.indianbankupi",
  "com.infrasofttech.mahaupi",
  "com.jio.myjio",
  "com.khaalijeb.inkdrops",
  "com.lcode.allahabadupi",
  "com.lcode.corpupi",
  "com.lcode.csbupi",
  "com.lcode.dlbupi",
  "com.lcode.smartz",
  "com.lcode.ucoupi",
  "com.lvbank.upaay",
  "com.mgs.hsbcupi",
  "com.mgs.induspsp",
  "com.mgs.obcbank",
  "com.microlucid.mudrapay.android",
  "com.mipay.in.wallet",
  "com.mobikwik_new",
  "com.mobileware.upipsb",
  "com.myairtelapp",
  "com.mycompany.kvb",
  "com.olive.andhra.upi",
  "com.olive.dcb.upi",
  "com.omegaon_internet_pvt_ltd",
  "com.phonepe.app",
  "com.rblbank.mobank",
  "com.rblbank.upi",
  "com.realmepay.payments",
  "com.sbi.upi",
  "com.snapwork.hdfc",
  "com.truecaller",
  "com.udma.yuvapay.app",
  "com.ultracash.payment.customer",
  "com.upi.axispay",
  "com.upi.federalbank.org.lotza",
  "com.whatsapp",
  "in.amazon.mShop.android.shopping",
  "in.cointab.app",
  "in.org.npci.upiapp",
  "money.bullet",
  "net.one97.paytm"
];
