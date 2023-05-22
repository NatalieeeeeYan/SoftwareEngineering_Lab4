
<template>
  <q-layout view="hHh lpR fFf">

    <q-page-container>
      <router-view />

      <div>
        <q-dialog v-model="addressPage" no-click-outside-close>
          <q-layout view="Lhh lpR fff" container class="bg-white">
            <q-header class="bg-primary">
              <q-toolbar>
                <q-btn flat @click="drawer = !drawer" round dense icon="menu" />
                <q-toolbar-title>我的收货地址</q-toolbar-title>
                <q-btn outline rounded color="deep-purple-6" label="管理收货地址" style="margin-top: 1%;" @click="setting()" />
                <q-btn flat @click="drawerR = !drawerR" round dense icon="menu" />
                <q-btn flat v-close-popup round dense icon="close" @click="layout = false" />
              </q-toolbar>
            </q-header>

            <q-footer class="bg-black text-white">
              <q-toolbar inset>
                <q-toolbar-title></q-toolbar-title>
              </q-toolbar>
            </q-footer>

            <q-page-container>
              <q-page padding>
                <div class="row">
                  <q-btn outline rounded color="deep-purple-6" label="添加收货地址" style="margin-top: 2%;"
                    @click="add_Add_layout = true" />
                </div>
                <div v-for="add in address" :key="add" class="add-info" style="overflow: hidden; flex-grow: 9;" clickable
                  @click="selectNewAddress(add)">
                  <q-card flat bordered style="width: 100%; max-width: 100%; margin-top: 3%;">
                    <q-card-section style="overflow: hidden; width: 100%;">

                      <div class="card-box" style="display: flex;">
                        <div class="logo"
                          style="display: flex; flex-direction: column; justify-content: center; align-items: center;">
                          <div class="defaultLogo" v-if="add.isDefault"
                            style="flex-grow: 1; display: flex; align-items: center;">
                            <q-btn flat round color="primary" icon="local_activity" />
                          </div>
                          <div class="nameLogo" style="flex-grow: 1; display: flex; align-items: center;">
                            <q-btn round color="deep-purple-4" push>
                              <div class="row items-center no-wrap">
                                <div class="text-center" style="font-weight: bold; font-size: large;">
                                  {{ add.name[0] }}
                                </div>
                              </div>
                            </q-btn>
                          </div>
                        </div>

                        <div style="float: left; margin-left: 2%; margin-top: 2%; width: 80%;">
                          <div style="width: 100%; overflow: hidden;">
                            <div style="margin-left: 2%; float: left; font-weight: bolder; font-size: large;">
                              收货人：
                              {{ add.name }}
                            </div>
                            <div style="margin-left: 3%; float: left; color: rgb(190, 70, 40); margin-top: 3px;">
                              联系方式：
                              {{ add.phoneNumber }}
                            </div>
                          </div>
                          <div style="width: 100%; margin-top: 2%;">
                            <div style="margin-left: 2%;">
                              收货地址：
                              {{ add.address }}
                            </div>
                          </div>
                        </div>
                      </div>

                    </q-card-section>
                  </q-card>
                </div>
              </q-page>
            </q-page-container>
          </q-layout>
        </q-dialog>

        <q-dialog v-model="add_Add_layout" no-click-outside-close>
          <q-layout view="Lhh lpR fff" container class="bg-white">
            <q-header class="bg-primary">
              <q-toolbar>
                <q-btn flat @click="drawer = !drawer" round dense icon="menu" />
                <q-toolbar-title>添加收货地址</q-toolbar-title>
                <q-btn flat @click="drawerR = !drawerR" round dense icon="menu" />
                <q-btn flat v-close-popup round dense icon="close" @click="add_Add_layout = false" />
              </q-toolbar>
            </q-header>

            <q-page-container>
              <q-page padding>
                <q-form ref="myForm" @submit="submitNewAddress" @reset="onReset" class="q-gutter-md">
                  <br>
                  <q-input filled v-model="recipient" label="收货人姓名 *" hint="Recipient Name" lazy-rules :rules="[val => val && val.length > 0 && val.length < 13 || '请输入收货人名称',
                  ]" />

                  <q-input filled v-model="rec_tele" label="手机号码 *" hint="Recipient's phone number" lazy-rules :rules="[
                    (val) => (val && val.length > 0) || '请输入收货人手机号',
                    (val) => val.length === 11 || '请输入正确的手机号',
                  ]" @click="code = 0" />

                  <q-input filled v-model="rec_address" label="收货地址 *" hint="Recipient's address" lazy-rules
                    :rules="[val => val && val.length > 0 || '请输入的收货地址']"></q-input>

                  <q-toggle v-model="set_this_add_default" checked-icon="check" color="red" label="设置为默认地址"
                    unchecked-icon="clear" />

                  <div class="submit">
                    <q-btn label="Submit" type="submit" color="red-4" />
                    <q-btn label="Reset" type="reset" color="red-4" flat class="q-ml-sm" />
                  </div>
                </q-form>
              </q-page>
            </q-page-container>
          </q-layout>
        </q-dialog>
      </div>
      <q-dialog v-model="layout" no-click-outside-close>
        <q-layout view="Lhh lpR fff" container class="bg-white">
          <q-header class="bg-primary">
            <q-toolbar>
              <q-btn flat @click="drawer = !drawer" round dense icon="menu" />
              <q-toolbar-title>金额明细</q-toolbar-title>
              <div class="text-red text-caption"></div>
              <q-btn flat @click="drawerR = !drawerR" round dense icon="menu" />
              <q-btn flat v-close-popup round dense icon="close" @click="layout = false" />
            </q-toolbar>
          </q-header>

          <q-page-container>
            <q-page padding>
              <div style="width:100%" class="row">
                <div class="row" style="width:25%" v-for="commodity in order_lst" :key="commodity">
                  <div class="row" style="width:100%">
                    <q-img class="rounded-borders row" :src="commodity.goods.image[0]" style="width:80%;"></q-img>
                  </div>
                </div>
              </div>

              <div style="width:100%">
                <div>
                  <q-chip size="lg" icon="bookmark">
                    商品总价: ¥{{ total }}
                  </q-chip>
                  <q-chip size="lg" icon="bookmark">
                    优惠: ¥{{ total_discount }}
                  </q-chip>
                </div>
                <div>
                  <q-chip size="lg" icon="bookmark">
                    商品合计: ¥{{ total_event }}
                  </q-chip>
                </div>
                <div>
                  <q-btn-dropdown color="primary" label="满减明细" style="width:100%">
                    <div v-for="single_event in event_detail" :key="single_event" style="width:100%">
                      <q-card style="width:100%;">
                        <div class="row" style="width:100%">
                          <div class="row" style="width:20%" v-for="single_commodity in single_event"
                            :key="single_commodity">
                            <div style="width:100%">
                              <q-img :src="single_commodity.goods.image[0]"
                                style="width:90%;margin-top: 5%;margin-bottom: 5%;"></q-img>
                            </div>
                          </div>
                        </div>
                        <div class="row">
                          <div>
                            <q-chip outline color="orange" text-color="white" icon-right="star">
                              满{{ single_event[0].event.amount }}减{{ single_event[0].event.discount }}
                            </q-chip>
                          </div>
                          <div style="margin-top:2%">
                            小计: ¥{{ single_event[0].event.event_price }}
                          </div>
                          <div style="margin-top:2%;margin-left: 1%;">
                            优惠: ¥{{ single_event[0].event.event_price - single_event[0].event.event_price_after }}
                          </div>
                        </div>
                      </q-card>
                      <!-- <div v-for="single_commodity in single_event" :key="single_commodity">

                      </div> -->
                    </div>
                  </q-btn-dropdown>
                </div>
              </div>
            </q-page>
          </q-page-container>
        </q-layout>
      </q-dialog>

      <div class="row flex">
        <div style="width:30%; margin-top: 5%;">
          <q-img :src="imglist[2]" />
          <q-img :src="imglist[0]" />
        </div>
        <div style="width:70%">
          <q-card style="width:100%; height:670px" class="bg">
            <br />
            <q-card style="width:97%; margin: auto; height:100%">
              <!--地址选择-->
              <br />
              <div style="margin-left:2%" class="text-h6">收货地址</div>
              <template v-if="isDefault == true">
                <div style="margin-left: 2%;" @click="select()">
                  <q-card class="my-card" flat bordered style="width: 80%; max-width: 100%; margin-top: 3%;">
                    <q-card-section style="overflow: hidden; width: 100%;">

                      <div class="card-box" style="display: flex;">
                        <div class="logo"
                          style="display: flex; flex-direction: column; justify-content: center; align-items: center;">
                          <div class="nameLogo" style="flex-grow: 1; display: flex; align-items: center;">
                            <q-btn round color="deep-purple-4" push>
                              <div class="row items-center no-wrap">
                                <div class="text-center" style="font-weight: bold; font-size: large;">
                                  {{ selectAddress.name[0] }}
                                </div>
                              </div>
                            </q-btn>
                          </div>
                        </div>

                        <div style="float: left; margin-left: 2%; margin-top: 2%; width: 80%;">
                          <div style="width: 100%; overflow: hidden;">
                            <div style="margin-left: 2%; float: left; font-weight: bolder; font-size: large;">
                              收货人：
                              {{ selectAddress.name }}
                            </div>
                            <div style="margin-left: 3%; float: left; color: rgb(190, 70, 40); margin-top: 3px;">
                              联系方式：
                              {{ selectAddress.phoneNumber }}
                            </div>
                          </div>
                          <div style="width: 100%; margin-top: 2%;">
                            <div style="margin-left: 2%;">
                              收货地址：
                              {{ selectAddress.address }}
                            </div>
                          </div>
                        </div>
                      </div>

                    </q-card-section>
                  </q-card>
                </div>
              </template>
              <template v-else>
                <div style="margin-left:2%">
                  <q-card class="my-card" flat bordered style="width: 80%; max-width: 100%; margin-top: 3%;">
                    <q-card-section style="overflow: hidden; width: 100%;">
                      <div class="row">
                        <q-btn round color="deep-purple-4" glossy icon="place" @click="select()" />
                        <div style="font-size: large; font-weight: bolder; margin-left:1%;margin-top:1%">
                          未选择收货地址
                        </div>
                      </div>
                    </q-card-section>
                  </q-card>
                </div>
              </template>

              <q-scroll-area style="height: 450px;">
                <div class="order-display" style="margin-top: 2%;">
                  <q-card>
                    <q-card-section class="items-center">
                      <div class="display-order-before-paying" v-for="commodity in order_lst" :key="commodity">
                        <div class="row">
                          <div class="merchant-order-text" style="width:350px">
                            <div class="text-overline text-grey">订单编号 {{ commodity.userOrderTemplate.id }}</div>
                            <div class="text-bold q-mt-sm q-mb-xs" style="font-size: large;">商品名称{{
                              commodity.goods.goodsName
                            }}</div>
                            <div class="row">
                              <div class="text-grey" style="margin-top: 2%;">商品价格</div>
                              <div class="text-bold text-red-10" style="font-size: large;">￥{{
                                commodity.goods.price
                              }}</div>
                            </div>
                            <div class="row">
                              <div class="text-grey" style="margin-top: 2%;">商品数量</div>
                              <div class=" text-black" style="font-size: large;margin-left: 5%;">{{
                                commodity.userOrderTemplate.quantity
                              }}</div>
                            </div>
                            <div class="row">
                              <div class="text-grey" style="margin-top: 2%;">店铺名称</div>
                              <div class="text-black" style="font-size: medium;margin-left: 5%;">{{
                                commodity.shop.shopName
                              }}</div>
                            </div>
                            <div class="row">
                              <div class="text-grey" style="margin-top: 2%;">实付单价</div>
                              <div class="text-bold text-red-10" style="font-size: large;">￥{{
                                commodity.userOrderTemplate.unitPrice
                              }}</div>
                            </div>
                          </div>
                          <div class="merchant-order-pict">
                            <q-img class="rounded-borders" :src="commodity.goods.image[0]"
                              style="width:200px; height:150px; margin-top: 10%;margin-bottom: 10%;margin-right: 80%;"></q-img>
                          </div>
                        </div>
                        <q-separator inset />

                      </div>
                      <div class="show-total-price">

                      </div>
                    </q-card-section>
                  </q-card>
                </div>
              </q-scroll-area>

            </q-card>
          </q-card>
        </div>

        <q-dialog v-model="show_paying" persistent>
          <q-card>
            <q-card-actions align="right">
              <q-btn flat label="取消支付" color="indigo-10" v-close-popup @click="payDelete()" />
              <q-btn flat label="确认支付" color="red-12" v-close-popup @click="payOrder()" />
            </q-card-actions>
          </q-card>
        </q-dialog>

        <q-dialog v-model="show_cancel_order_confirm" persistent>
          <q-card>
            <q-card-section class="row items-center">
              <q-avatar icon="warning" color="negative" text-color="white" />
              <span class="q-ml-sm">确定要取消订单吗？</span>
            </q-card-section>

            <q-card-actions align="right">
              <q-btn flat label="取消" color="indigo-10" v-close-popup />
              <q-btn flat label="确认" color="pink-13" v-close-popup @click="cancelOrder()" />
            </q-card-actions>
          </q-card>
        </q-dialog>

        <q-dialog v-model="show" persistent>
          <q-card>
            <q-card-section class="row items-center">
              <q-avatar icon="warning" color="negative" text-color="white" />
              <span class="q-ml-sm">确定要放弃付款吗？</span>
            </q-card-section>

            <q-card-actions align="right">
              <q-btn flat label="取消" color="indigo-10" v-close-popup />
              <q-btn flat label="确认" color="pink-13" v-close-popup />
            </q-card-actions>
          </q-card>
        </q-dialog>

      </div>
    </q-page-container>

    <q-footer elevated class="bg-primary text-white">
      <q-toolbar>
        <q-toolbar-title>
          <div>确认订单</div>
        </q-toolbar-title>
        <q-chip dense clickable color="purple-4" text-color="white" icon="directions" @click="View_details()">
          查看明细
        </q-chip>
        <div>
          合计:
        </div>
        <div class="text-orange-7 text-h4">
          ¥ {{ total_event }}
        </div>
        <q-btn flat color="grey-6" style=" font-size: large;" label="取消" @click="confirmCancelOrder()" />
        <q-btn flat style="color: #570089; font-size: large;" label="去支付" @click="goPaying()" />
      </q-toolbar>
    </q-footer>

  </q-layout>
</template>
<script setup>
import { ref, getCurrentInstance } from 'vue'
import axios from 'axios'
import { onMounted } from 'vue'
import chilling from '../../assets/imgs/chilling.png'
import sprinting from '../../assets/imgs/sprinting.png'
import dialog from '../../assets/imgs/dialog.png'
import { useRouter } from 'vue-router'

const router = useRouter()

const imglist = ref([sprinting, chilling, dialog])

const order_lst = ref({})
let show_cancel_order_confirm = ref(false)
let show = ref(false)

let show_paying = ref(false)

const axiosInstance = axios.create({
  baseURL: 'http://localhost:9999',
});
let response_register = ref(null)
let code = ref(null)
const address = ref(null)
const defaultAddress = ref(null)
const selectAddress = ref(null)
const isDefault = ref(false)
const addressPage = ref(false)
const instance = getCurrentInstance()
let userId = localStorage.getItem('userId')

// 收货地址相关的变量
let myForm = ref(null)
let add_Add_layout = ref(false)     // 打开新增地址的dialogue
let set_this_add_default = ref(false)   // 设置默认地址

let recipient = ref(null)
let rec_address = ref(null)
let rec_tele = ref(null)

const layout = ref(false)
const event_detail = ref(localStorage.getItem('event_detail'))
const total = localStorage.getItem('total')
const total_event = localStorage.getItem('total_event')
const total_discount = localStorage.getItem('total_discount')


function getTemplateOrder() {
  order_lst.value = []
  const orderTemplate = localStorage.getItem('orderTemplate')
  console.log("get template order: ", orderTemplate)
  axiosInstance.get(`/userOrderTemplate/ret?template=${orderTemplate}`).then((response) => {
    const r = response.data
    order_lst.value = r.data
    console.log('get template order: ', order_lst.value)
  });

}

function goPaying() {
  show_paying.value = true
  const result_list = []
  //下单日期
  let currentDate = new Date();
  let year = currentDate.getFullYear();
  let month = String(currentDate.getMonth() + 1).padStart(2, '0');
  let day = String(currentDate.getDate()).padStart(2, '0');

  let formattedDate = `${year}-${month}-${day}`;
  //来自购物车
  console.log('order_lst.value: ')
  console.log(order_lst.value)
  for (let i = 0; i < order_lst.value.length; i++) {
    order_lst.value[i].userOrderTemplate['addressId'] = selectAddress.value.id
    order_lst.value[i].userOrderTemplate['date'] = formattedDate
    result_list.push(order_lst.value[i].userOrderTemplate)
  }
  console.log('result_list: ')
  console.log(result_list)
  if (result_list[0].source == true) {
    axiosInstance.post('/userOrder/orderFromCart', result_list).then((response) => {
      const r = response.data
      console.log('r: ', r)
    });
  }
  else {
    axiosInstance.post('/userOrder/orderDirect', result_list).then((response) => {
      const r = response.data
      console.log('r: ', r)
    });
  }
}

function payOrder() {
  const result_list = []
  let currentDate = new Date();
  let year = currentDate.getFullYear();
  let month = String(currentDate.getMonth() + 1).padStart(2, '0');
  let day = String(currentDate.getDate()).padStart(2, '0');

  let formattedDate = `${year}-${month}-${day}`;

  console.log('order_lst.value: ')
  console.log(order_lst.value)
  for (let i = 0; i < order_lst.value.length; i++) {
    order_lst.value[i].userOrderTemplate['addressId'] = selectAddress.value.id
    order_lst.value[i].userOrderTemplate['date'] = formattedDate
    result_list.push(order_lst.value[i].userOrderTemplate)
  }
  console.log('result_list: ')
  console.log(result_list)

  axiosInstance.post('/userOrder/pay', result_list).then((response) => {
    const r = response.data
    console.log('r: ', r)
    if(r.code == 20010){
      alert('账户余额不足，支付失败')
    }
    else if(r.code == 20000){
      alert('支付成功')
      router.push({ path: '/accountInfo', query: { order: false } })
    }
  });
}

//取消订单
function confirmCancelOrder() {
  show_cancel_order_confirm.value = true
  console.log('confirm cancel order')
}

function cancelOrder() {
  console.log('cancel order')
  router.push('/userShoppingCart')
}
//取消支付
function payDelete() {
  console.log('pay delete')
  router.push({ path: '/accountInfo', query: { order: false } })
}
function select() {
  console.log("select address: ", selectAddress.value)
  addressPage.value = true
}

// 提交新增收货地址表单
function submitNewAddress() {
  console.log("新增收货地址时获取user id: ", userId, "\t电话号码: ", rec_tele.value,
    "\t地址：", rec_address.value, "\t默认地址：", set_this_add_default.value)
  axiosInstance.post('/user/addDeliveryAddress', {
    userId: userId,
    name: recipient.value,
    address: rec_address.value,
    phoneNumber: rec_tele.value,
    isDefault: Boolean(set_this_add_default.value),
  }, {
    params: {
      userId: userId,
      address: rec_address.value,
      phoneNumber: rec_tele.value,
      isDefault: Boolean(set_this_add_default.value),
    }
  }).then((response) => {
    console.log('Response data:', response.data);
    response_register.value = response.data;
    console.log(response_register);
    code.value = response_register.value['code'];
    set_this_add_default.value = false
    add_Add_layout.value = false
    getAllAddresses()

  }).catch((error) => {
    console.error('Error:', error);
  });

}

function getAllAddresses() {
  axiosInstance.get('/user/showPersonalDeliveryAddress', {
    params: {
      userId: localStorage.getItem('userId')
    }
  }).then((response) => {
    const r = response.data
    console.log('get user delivery address: ');

    address.value = r.data
    console.log(address.value)
    for (let i = 0; i < address.value.length; i++) {
      if (address.value[i].isDefault == true) {
        defaultAddress.value = address.value[i]
        selectAddress.value = address.value[i]
        console.log('default address: ')
        console.log(defaultAddress.value)
      }
    }
    instance.proxy.$forceUpdate();
  });


}

function selectNewAddress(address) {
  console.log('selectNewAddress: ', address)
  selectAddress.value = address
  isDefault.value = true
  addressPage.value = false
}

function setting() {
  console.log('setting')
  router.push({ path: '/accountInfo', query: { id: false } })
}

function View_details() {
  layout.value = true;
  console.log('event_detail')
  event_detail.value = JSON.parse(event_detail.value)
  console.log(event_detail.value)
  console.log(event_detail.value.goods)
}


onMounted(() => {
  getTemplateOrder()

  axiosInstance.get('/user/showPersonalDeliveryAddress', {
    params: {
      userId: localStorage.getItem('userId')
    }
  }).then((response) => {
    const r = response.data
    console.log('get user delivery address: ');

    address.value = r.data
    console.log(address.value)
    for (let i = 0; i < address.value.length; i++) {
      if (address.value[i].isDefault == true) {
        defaultAddress.value = address.value[i]
        selectAddress.value = address.value[i]
        isDefault.value = true
        console.log('default address: ')
        console.log(defaultAddress.value)
      }
    }
  });
});

</script>
<style scoped>
.bg {
  background-image: linear-gradient(to right top, #d16ba5, #c777b9, #ba83ca, #aa8fd8, #9a9ae1, #909ae6, #849aec, #769af1, #708ef7, #6f80fb, #7571fc, #7f5ffb);
}

.my-card {
  width: 100%;
  max-width: 350px;
  min-height: 70px;
}
</style>
