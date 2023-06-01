<template>
  <q-layout view="hHh lpR fFf">

    <q-header elevated class="bg-primary text-white">
      <q-toolbar>
        <q-btn dense flat round icon="menu" @click="toggleLeftDrawer" />
        <q-toolbar-title>
          <q-avatar>
            <img src="https://cdn.quasar.dev/logo-v2/svg/logo-mono-white.svg">
          </q-avatar>
          购物车
        </q-toolbar-title>
        <q-btn-group outline>
        <q-btn color="grey-4" text-color="purple" glossy unelevated icon="settings" @click="settings()">{{ setting_lable
        }}</q-btn>


        <q-btn label="返回首页" color="purple" to="/user"></q-btn>
      </q-btn-group>
      </q-toolbar>

    </q-header>

    <q-page-container>
      <router-view />

      <q-dialog v-model="layout" no-click-outside-close>
        <q-layout view="Lhh lpR fff" container class="bg-white">
          <q-header class="bg-primary">
            <q-toolbar>
              <q-btn flat @click="drawer = !drawer" round dense icon="menu" />
              <q-toolbar-title>金额明细</q-toolbar-title>
              <div class="text-red text-caption"> 实际优惠金额请以下单页为准</div>
              <q-btn flat @click="drawerR = !drawerR" round dense icon="menu" />
              <q-btn flat v-close-popup round dense icon="close" @click="layout = false" />
            </q-toolbar>
          </q-header>

          <q-page-container>
            <q-page padding>
              <div style="width:100%" class="row">
                <div class="row" style="width:25%" v-for="selectCommodity in selectedCommoditiesObjects"
                  :key="selectCommodity">
                  <div class="row" style="width:100%">
                    <q-img class="rounded-borders row" :src="selectCommodity.goods.image[0]" style="width:80%;"></q-img>
                    <q-checkbox v-model="selectedCommodities[selectCommodity.goods.id]" @click="calculate(1)" />
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

      <div class="q-pa-md row items-start q-gutter-md" style="width:100%">
        <!--用户信息展示-->
        <!-- <q-card class="my-card" flat bordered style="width: 280px">
          <q-card-section horizontal>
            <q-card-section class="q-pt-xs">
              <div class="text-overline">Overline</div>
              <div class="text-h5 q-mt-sm q-mb-xs">{{ username }}</div>
              <div class="text-caption text-grey">

              </div>
            </q-card-section>

            <q-card-section class="col-5 flex flex-center">
              <q-img :src="url" style="border-radius: 50%;" />
            </q-card-section>
          </q-card-section>

          <q-separator />

          <q-card-actions>
            <q-btn flat icon="logout" color="accent" to="/login">
              Logout
            </q-btn>
            <q-btn flat icon="settings" color="accent" to="/accountInfo">
              Settings
            </q-btn>
          </q-card-actions>
        </q-card> -->

        <div class="q-pa-ma" style="width:30%;">
          <div style="width:300px;">
            <q-img :src="url" style="border-radius: 50%; left:50px" />
            <div class="text-h5 q-mt-sm q-mb-xs" style="margin-left:70px"> {{ username }}</div>

            <q-separator style="margin-left:50px" />
            <div class="col-auto text-grey text q-pt-md row no-wrap items-center" style="margin-left:70px">
              <q-icon name="place" />
              Shanghai, China
            </div>
          </div>
        </div>

        <!--购物车商品展示-->
        <div class="q-pa-ma" style="width:65%">
          <div>
            <q-tabs v-model="tab" dense class="text-grey" active-color="primary" indicator-color="primary" align="justify"
              narrow-indicator>
              <q-tab name="Commodity" label="Commodity" />
            </q-tabs>

            <q-separator />

            <q-tab-panels v-model="tab" animated>
              <q-tab-panel name="Commodity">
                <div class="flex justify-center">
                  <q-scroll-area :visible="visible" style="height: 650px; width: 900px;" class="flex justify-center">
                    <div class="flex q-justify-around" style="width: 900px;">
                      <div v-for="commodity in commodities" :key="commodity" class="flex q-py-xs justify-around"
                        style="width: 900px;">
                        <q-card class="shop-card" flat bordered>
                          <q-card-section horizontal>
                            <q-card-section class="q-pt-xs" style="width:60%; height:90%">
                              <div class="text-overline">Overline</div>
                              <div class="row">
                                <q-btn class="text-h5 q-mt-sm q-mb-xs" flat @click="toCommodity(commodity.goods.id)">{{
                                  commodity.goods.goodsName }}</q-btn>
                                <q-chip style="margin-top: 2%;"
                                  v-if="commodity.goods.eventId != 0 && commodity.event.status == 1 && !commodity.cartGoods.isRemoved"
                                  outline square color="deep-orange-7" text-color="white" icon="directions">
                                  满{{ commodity.event.amount }}减{{ commodity.event.discount }}
                                </q-chip>
                              </div>
                              <div class="text-caption text-grey" style="height:20px">
                                {{ commodity.goods.description }}
                              </div>
                            </q-card-section>
                            <q-card-section>

                            </q-card-section>

                            <q-card-section class="col-5 flex justify-end">
                              <q-img class="rounded-borders" :src="commodity.goods.image[0]"
                                style="width:200px; height:150px"></q-img>
                            </q-card-section>
                          </q-card-section>

                          <q-card-section>
                            <div class="row">
                              <div class="text-orange text-h5"> ¥ {{ commodity.goods.price }}</div>
                              <q-chip
                                v-if="commodity.goods.price != commodity.goods.event_price && commodity.event.status == 1"
                                color="orange" text-color="white" icon="alarm" label="活动价格">
                                ¥ {{ commodity.goods.event_price }}</q-chip>
                            </div>
                          </q-card-section>

                          <q-card-section>
                            <template v-if="!commodity.cartGoods.isRemoved">
                              <div class="flex">
                                <q-btn class="decrement" icon="remove" size="xs" @click="decrement(commodity)" />
                                <div class="justify-center" style="width:30px; display:flex; align-items:center">{{
                                  commodity.cartGoods.goodsQuantity }}</div>
                                <q-btn class="increment" icon="add" size="xs" @click="increment(commodity)" />
                              </div>
                            </template>
                            <template v-else>
                              <div class="text-red">已下架</div>
                            </template>
                          </q-card-section>

                          <q-separator />


                          <q-card-actions>
                            <q-card-section horizontal>
                              <q-checkbox v-model="selectedCommodities[commodity.goods.id]"
                                v-if="!commodity.cartGoods.isRemoved || setting" @click="calculate(0)" />
                            </q-card-section>
                            <q-btn v-if="setting" flat color="primary" @click="singel_delete(commodity.goods.id)">
                              Delete
                            </q-btn>
                          </q-card-actions>
                        </q-card>
                      </div>
                    </div>
                  </q-scroll-area>
                </div>
              </q-tab-panel>
            </q-tab-panels>
          </div>
        </div>
        <div>
          <q-btn v-if="setting" round color="purple" icon="delete" class="absolute" to="/userShoppingCart"
            @click="batch_delete" style="top: 550px; right: 50px; transform: translateY(-50%);" size="30px">
          </q-btn>
        </div>
      </div>


    </q-page-container>

    <template v-if="!setting">
      <q-footer elevated class="bg-blue-grey-1 text-black" style="height:10%">

        <q-toolbar>
          <q-toolbar-title>

          </q-toolbar-title>


          <div class="row" style="height:100%">
            <div style="margin-top:35px;" class="text-grey">
              已经选 {{ total_number }} 件，
            </div>
            <div style="margin-top:35px;" class="text-grey">
              共减 ¥ {{ total_discount }}，
            </div>

            <div style="margin-top:30px;">
              <q-chip dense clickable color="purple-4" text-color="white" icon="directions" @click="View_details()">
                查看明细
              </q-chip>
            </div>
            <div style="margin-top: 34px;">
              合计:
            </div>
            <div class="text-orange-7 text-h4" style="margin-top:17px">
              ¥ {{ total_event }}
            </div>
          </div>
          <q-btn style=" color: white;margin-left:2%;margin-top:10px" class="bg-purple-4" label="结算" @click="order" />
        </q-toolbar>

      </q-footer>
    </template>
  </q-layout>
</template>

<script setup>
import { ref, getCurrentInstance } from 'vue'
import axios from 'axios'
import { onMounted } from 'vue'
import { useRouter } from 'vue-router'

const url = ref('https://avatars.githubusercontent.com/u/105032850?s=400&u=285d7d130058e413bb8797cb52bc10f75c343076&v=4')
const username = localStorage.getItem('username')
const tab = ref("Commodity")
const instance = getCurrentInstance()
const userId = localStorage.getItem('userId')
const layout = ref(false)
const setting = ref(false)
const setting_lable = ref('管理')
const total_number = ref(0)
let selectedCommoditiesObject = [] //选中的商品对象
let selectedCommoditiesObjects = [] //在金额明细中不更新

const axiosInstance = axios.create({
  baseURL: 'http://120.46.154.28:9999/',
});

const commodities = ref([])
const router = useRouter()

const selectedCommodities = ref([]) // 存储选中的商品
const total = ref(0) //原始总价
const total_event = ref(0) //活动后总价

const total_discount = ref(0) //总优惠价格
let result_list = [] //返回的list
const event_number = ref([])
let event_price = {} //活动id对应的未减少价格
let event_price_after = {} //活动id对应的减少后价格
let event_detail = {}

function settings() {
  setting.value = !setting.value;
  if (setting.value) {
    setting_lable.value = '退出管理'
  }
  else {
    setting_lable.value = '管理'
  }
}

function singel_delete(id) {
  axiosInstance.delete(`/cart/deleteBatch?goodsId=${id}&userId=${userId}`).then((res) => {
    console.log("single")
    console.log(res.data)
    update()
  });
}

function batch_delete() {
  console.log(selectedCommodities.value)
  const selectedCommodityIds = Object.entries(selectedCommodities.value)
    .filter(([key, value]) => value === true)
    .map(([key, value]) => parseInt(key));

  console.log("selectedCommodityIds")
  console.log(selectedCommodityIds);

  axiosInstance.delete(`/cart/deleteMulti?goodsIdList=${selectedCommodityIds}&userId=${userId}`).then((res) => {
    console.log("batch")
    console.log(res.data)
    console.log(selectedCommodities.value)
    selectedCommodities.value = [];
    update()
  });
}

function calculate(number) {
  console.log("calculate")
  console.log(selectedCommodities.value)

  //初始化满减后商品单价
  for (let i = 0; i < commodities.value.length; i++) {
    commodities.value[i].goods['event_price'] = commodities.value[i].goods['price']
  }
  event_detail = {}

  //选中的商品id
  const selectedCommodityIds = Object.entries(selectedCommodities.value)
    .filter(([key, value]) => value === true)
    .map(([key, value]) => parseInt(key));

  console.log(selectedCommodityIds)

  console.log(commodities.value)

  let events = [] //选中商品的活动id
  selectedCommoditiesObject = [] //选中的商品对象
  total.value = 0
  total_event.value = 0
  total_discount.value = 0
  total_number.value = 0
  result_list = [] //返回的list

  for (let i = 0; i < commodities.value.length; i++) {
    if (selectedCommodityIds.includes(commodities.value[i].goods.id)) {
      total.value += commodities.value[i].goods.price * commodities.value[i].cartGoods.goodsQuantity
      total_number.value += commodities.value[i].cartGoods.goodsQuantity
      //活动id不为0，并且正在举行
      if (commodities.value[i].event.id != 0 && commodities.value[i].event.status == 1) {
        events.push(commodities.value[i].event.id)
      }
      selectedCommoditiesObject.push(commodities.value[i])
    }
  }

  console.log('selectedCommoditiesObject')
  console.log(selectedCommoditiesObject)
  if (number == 0) {
    selectedCommoditiesObjects = Array.from(selectedCommoditiesObject)
  }

  //对活动id去重
  let uniqueList = events.filter((value, index, self) => {
    return self.indexOf(value) === index;
  });

  uniqueList.push(0)
  event_number.value = uniqueList
  // console.log("uniqueList")
  // console.log(uniqueList)

  event_price = {} //活动id对应的未减少价格
  event_price_after = {} //活动id对应的减少后价格

  //uniqueList 正在举办的活动list
  //初始化活动id对应的价格 [0:0,1:0,2:0,3:0]  0表示不受
  for (let i = 0; i < uniqueList.length; i++) {
    event_price[uniqueList[i]] = 0
  }
  event_price[0] = 0
  console.log('uniqueList')
  console.log(uniqueList)

  console.log('selectedCommoditiesObject')
  console.log(selectedCommoditiesObject)



  //计算每个活动的价格
  for (let i = 0; i < uniqueList.length; i++) {
    for (let j = 0; j < selectedCommoditiesObject.length; j++) {
      // console.log('c single')
      // console.log(uniqueList[i])
      // console.log(selectedCommoditiesObject[j].event.id)
      // console.log(selectedCommoditiesObject[j].event.status)
      console.log('i:', i)
      console.log('j:', j)
      //如果所选商品参与活动 并且活动已经开始 累加单个活动的价格
      if (selectedCommoditiesObject[j].event.id == uniqueList[i] && selectedCommoditiesObject[j].event.status == 1 && selectedCommoditiesObject[j].event.id != 0) {
        event_price[uniqueList[i]] += selectedCommoditiesObject[j].goods.price * selectedCommoditiesObject[j].cartGoods.goodsQuantity
        console.log('event_price[uniqueList[i]]')
        console.log(event_price[uniqueList[i]])
      }
    }
  }

  for(let i=0; i<selectedCommoditiesObject.length; i++){
    if(selectedCommoditiesObject[i].event.id == 0 || selectedCommoditiesObject[i].event.status != 1){
      event_price[0] += selectedCommoditiesObject[i].goods.price * selectedCommoditiesObject[i].cartGoods.goodsQuantity
    }
  }
  console.log('event_price')
  console.log(event_price)
  const promises = [];

  for (let key in event_price) {
    console.log('key')
    console.log(key)
    console.log('event_price[key]')
    console.log(event_price[key])
    if (key == 0) {
      event_price_after[key] = event_price[key];
    } else {
      const promise = axiosInstance.get('/event/show', {
        params: {
          eventId: key
        }
      }).then((res) => {
        const data = res.data.data;
        const amount = data['amount'];
        const discount = data['discount'];

        const multiple = Math.floor(event_price[key] / amount);
        event_price_after[key] = event_price[key] - multiple * discount;
      });

      promises.push(promise);
    }
  }

  // 使用 Promise.all 等待所有异步请求完成
  Promise.all(promises)
    .then(() => {

      console.log('event_price_after')
      console.log(event_price_after)
      for (let key in event_price_after) {
        total_event.value += event_price_after[key];
      }
      total_discount.value = total.value - total_event.value

      let event_commodity_list = {} //活动id对应的商品list
      //event_commodity_list = {0:[],1:[],2:[],3:[]} 0表示不参加活动
      for (let i = 0; i < uniqueList.length; i++) {
        let list = []
        for (let j = 0; j < selectedCommoditiesObject.length; j++) {
          if (selectedCommoditiesObject[j].event.id == uniqueList[i]) {
            list.push(selectedCommoditiesObject[j])
          }
        }
        event_commodity_list[uniqueList[i]] = list
      }
      let list = []
      for (let j = 0; j < selectedCommoditiesObject.length; j++) {
        if (selectedCommoditiesObject[j].event.status != 1 || selectedCommoditiesObject[j].event.id == 0) {
          list.push(selectedCommoditiesObject[j])
        }
      }
      event_commodity_list[0] = list

      console.log('event_commodity_list: ', event_commodity_list);

      console.log('result_list')
      console.log(event_commodity_list)

      //TODO: 计算商品的价格
      for (let i in event_commodity_list) {
        console.log('i')
        console.log(i)
        let list = event_commodity_list[i]
        let single_event_discount = event_price[i] - event_price_after[i]
        let event_discounted = 0 //已经减少的价格

        //下单日期
        let currentDate = new Date();
        let year = currentDate.getFullYear();
        let month = String(currentDate.getMonth() + 1).padStart(2, '0');
        let day = String(currentDate.getDate()).padStart(2, '0');

        let formattedDate = `${year}-${month}-${day}`;
        console.log('date')
        console.log(formattedDate)

        //保证加起来的和等于总的减少的价格，最后一个商品的价格等于总的减少的价格减去前面的商品价格
        for (let j = 0; j < list.length - 1; j++) {
          let single_commodities = list[j]

          //单种商品的价格，即totalPrice
          let single_discount = (single_commodities.goods.price * single_commodities.cartGoods.goodsQuantity) / event_price[i] * single_event_discount
          event_discounted += single_discount
          let single_commodity_total_price = single_commodities.goods.price * single_commodities.cartGoods.goodsQuantity - single_discount
          single_commodity_total_price = parseFloat(single_commodity_total_price.toFixed(2))
          let unitPrice = parseFloat((single_commodity_total_price / single_commodities.cartGoods.goodsQuantity).toFixed(2))

          console.log('single_discount: ');
          console.log(single_discount);
          console.log('single_commodity_total_price: ');
          console.log(single_commodity_total_price);

          let result_obj =
          {
            "eventId": single_commodities.event.id,
            "goodsId": single_commodities.goods.id,
            "quantity": single_commodities.cartGoods.goodsQuantity,
            "shopId": single_commodities.goods.shopId,
            "source": true,
            "totalPrice": single_commodity_total_price,
            "unitPrice": unitPrice,
            "userId": userId,
          }


          result_list.push(result_obj)

          //满减后价格加入原来的商品列表
          for (let k = 0; k < commodities.value.length; k++) {
            if (commodities.value[k].goods.id == single_commodities.goods.id) {
              commodities.value[k].goods.event_price = unitPrice
            }
          }

        }
        //最后一个商品的价格等于总的减少的价格减去前面的商品价格
        if (list.length > 0) {
          let last_commodities = list[list.length - 1]
          let last_commodity_total_price = (last_commodities.goods.price * last_commodities.cartGoods.goodsQuantity - (single_event_discount - event_discounted))
          let last_unitPrice = parseFloat((last_commodity_total_price / last_commodities.cartGoods.goodsQuantity).toFixed(2))
          let result_obj = {
            "eventId": last_commodities.event.id,
            "goodsId": last_commodities.goods.id,
            "quantity": last_commodities.cartGoods.goodsQuantity,
            "shopId": last_commodities.goods.shopId,
            "totalPrice": last_commodity_total_price,
            "unitPrice": last_unitPrice,
            "userId": userId,
            "source": true
          }
          console.log('last_commodity_total_price: ');
          console.log(last_commodity_total_price);
          console.log('last_unitPrice: ');
          console.log(last_unitPrice);
          result_list.push(result_obj)

          //满减后价格加入原来的商品列表
          for (let k = 0; k < commodities.value.length; k++) {
            if (commodities.value[k].goods.id == last_commodities.goods.id) {
              commodities.value[k].goods.event_price = last_unitPrice
            }
          }
        }

        console.log('commodity_event_after')
        console.log(commodities.value)

        //event_detail={id:[obj,obj,obj]} 没有0
        for (let i = 0; i < uniqueList.length; i++) {
          if (uniqueList[i] != 0) {
            event_detail[uniqueList[i]] = []
            for (let j = 0; j < selectedCommoditiesObject.length; j++) {
              if (selectedCommoditiesObject[j].event.id == uniqueList[i]) {
                selectedCommoditiesObject[j].event.event_price = event_price[selectedCommoditiesObject[j].event.id]
                selectedCommoditiesObject[j].event.event_price_after = event_price_after[selectedCommoditiesObject[j].event.id]
                event_detail[uniqueList[i]].push(selectedCommoditiesObject[j])
              }
            }
          }
        }
        console.log('selectedCommoditiesObjects')
        console.log(selectedCommoditiesObjects)
        console.log('event_detail')
        console.log(event_detail)
        console.log(uniqueList)

      }

    })
    .catch((error) => {
      // 处理异常情况
      console.error(error);
    });

}

function decrement(commodity) {
  console.log("decrement")
  console.log(commodity)
  axiosInstance.put(`/cart/deleteSingle?goodsId=${commodity.goods.id}&userId=${userId}`).then((res) => {
    // console.log("cart")
    // console.log(res.data)
    update()
  });
}

function increment(commodity) {
  // console.log("increment")
  // console.log(commodity)
  axiosInstance.post(`/cart/add2cart?goodsId=${commodity.goods.id}&userId=${userId}`).then((res) => {
    // console.log("cart")
    // console.log(res.data)

    update()
  });

}

function numberToHexColor(number) {

  const color = ["teal", "orange", "blue-grey", 'deep-purple', "green", "cyan", "blue", "purple", "pink", "indigo", 'lime']
  for (let i = 0; i < commodities.value.length; i++) {

  }
  let index = number % color.length
  let offset = number % 10 + 5
  offset = offset.toString(10)
  return color[index] + '-' + offset
}

function View_details() {
  layout.value = true;
  selectedCommoditiesObjects = Array.from(selectedCommoditiesObject)
}

function order() {
  console.log('order')
  console.log(result_list)
  if (result_list.length == 0) {
    alert("请先选择商品")
    return
  }
  else {
    axiosInstance.post('/userOrderTemplate/order', result_list).then((res) => {
      console.log(res.data)
      localStorage.setItem('orderTemplate', res.data.data);
      console.log(localStorage.getItem('orderTemplate'))
      localStorage.setItem('event_detail', JSON.stringify(event_detail));
      console.log('event_details_localStorage')
      console.log(localStorage.getItem('event_detail'))
      localStorage.setItem('total', total.value);
      localStorage.setItem('total_discount', total_discount.value);
      localStorage.setItem('total_event', total_event.value);
      router.push({ path: '/order' })
    });
  }

}

function update() {
  axiosInstance.get('/cart/showList',
    {
      params: {
        userId: userId
      }
    }).then((res) => {
      // console.log("res")
      // console.log(res.data.data)
      commodities.value = res.data.data
      console.log('commodities')
      console.log(commodities.value)
      //初始化满减后商品单价
      for (let i = 0; i < commodities.value.length; i++) {
        commodities.value[i].goods['event_price'] = commodities.value[i].goods['price']
      }
      console.log('commodities')
      console.log(commodities.value)
      calculate()
    });

  instance.proxy.$forceUpdate();
}

function toCommodity(id) {
  localStorage.setItem('commodityId', id);
  router.push('/commodity')
}

onMounted(() => {
  //userId全局变量
  axiosInstance.get('/cart/showList',
    {
      params: {
        userId: userId
      }
    }).then((res) => {
      // console.log("res")
      // console.log(res.data.data)
      commodities.value = res.data.data
      console.log('commodities')
      console.log(commodities.value)
      //初始化满减后商品单价
      for (let i = 0; i < commodities.value.length; i++) {
        commodities.value[i].goods['event_price'] = commodities.value[i].goods['price']
      }
      console.log('commodities')
      console.log(commodities.value)
    });

  // 计算距离下一个凌晨12点的时间间隔
  const now = new Date();
  const nextMidnight = new Date(now.getFullYear(), now.getMonth(), now.getDate() + 1, 0, 0, 0);
  const timeUntilMidnight = nextMidnight - now;
  console.log("now time")
  console.log(timeUntilMidnight)

  // 设置定时器，在凌晨12点触发刷新
  setTimeout(update, timeUntilMidnight);

  // 每隔24小时执行一次刷新 24 * 60 * 60 * 1000
  setInterval(update, 24 * 60 * 60 * 1000);
});
</script>

<style lang="sass" scoped>
.shop-card
  width: 100%
  width: 700px
  height: 100%
  max-height: 350px

</style>
