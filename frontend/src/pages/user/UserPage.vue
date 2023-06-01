<template>
  <q-layout view="hHh lpR fFf">

    <q-header elevated class="bg-primary text-white">
      <q-toolbar>
        <q-btn dense flat round icon="menu" @click="toggleLeftDrawer" />

        <q-toolbar-title>
          <q-avatar>
            <img src="https://cdn.quasar.dev/logo-v2/svg/logo-mono-white.svg">
          </q-avatar>
          用户首页
        </q-toolbar-title>
        <q-input dark dense standout v-model="text" input-class="text-right" class="q-ml-md">
          <template v-slot:append>
            <q-icon name="search" clickable @click="search()" />
          </template>
        </q-input>
      </q-toolbar>
    </q-header>

    <q-page-container>
      <router-view />
      <div class="q-pa-md row items-start q-gutter-md">
        <div class="left-side-information" style="display: flex; width: 30%; margin-top: 3%; flex-direction: column;">
          <!--用户信息展示-->
          <q-card class="my-card" flat bordered style="margin-top: 2%; flex-grow: 1;">
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

            <q-btn flat color="secondary" label="查看商品" style="margin-left: 0.5%;"
              @click="show_commodity = true; show_activity = false;" />

            <q-btn flat color="secondary" label="查看商城活动" style="margin-left: 0.5%;"
              @click="show_commodity = false; show_activity = true;" />
            <q-separator style="margin-top: 3%;" />

            <q-card-actions>
              <q-btn flat icon="person" color="accent" to="/accountInfo">
                个人主页
              </q-btn>
              <q-btn flat icon="logout" color="accent" to="/login">
                退出登录
              </q-btn>
            </q-card-actions>
          </q-card>

        </div>

        <div v-if="show_commodity" style="width: 65%;">

          <!--所有商品以及店铺信息展示-->
          <div v-if="!display_activity_commos" class="q-pa-ma">
            <div>
              <q-tabs v-model="tab" dense class="text-grey-10" active-color="primary" indicator-color="primary"
                align="justify" narrow-indicator>
                <q-tab name="Commodity" label="推荐商品" />
                <q-tab name="Shops" label="所有店铺" />
              </q-tabs>

              <q-separator />

              <q-tab-panels v-model="tab" animated>
                <q-tab-panel name="Commodity">
                  <!--商品信息展示---->
                  <div class="text-h6">Commodity</div>
                  <div class="flex justify-center">
                    <q-scroll-area :visible="visible" style="height: 620px; width: 800px;" class="flex justify-center">
                      <q-spinner-pie v-if="loading" color="orange" size="5.5em"/>
                      <div class="flex q-justify-around" style="width: 800px;">
                        <div v-for="commodity in commodities" :key="commodity" class="flex q-py-xs justify-around"
                          style="width: 400px;">
                          <q-card class="my-card">
                            <div class="q-pa-md">
                              <q-carousel class="commodity_img" swipeable animated v-model="commodity.goods.slide"
                                height="200px" thumbnails infinite>
                                <q-carousel-slide v-for="(image, index) in commodity.image" :key="index" :name="index + 1"
                                  :img-src="image" />
                              </q-carousel>
                            </div>

                            <q-card-section>
                              <div class="row no-wrap items-center">
                                <q-btn flat class="text-h6" @click="toCommodity(commodity.goods.id)">
                                  {{ commodity.goods.goodsName }}
                                </q-btn>
                                <q-chip v-if="commodity.event.id!=0 && commodity.event.status ==1" outline color="orange" text-color="white" icon-right="star">
                                  满{{ commodity.event.amount }}减{{ commodity.event.discount }}
                                </q-chip>
                              </div>

                              <q-rating v-model="commodity.goods.stars" :max="5" size="32px" />
                            </q-card-section>

                            <q-card-section class="q-pt-none">
                              <div class="text-subtitle1">
                                ¥ price: {{ commodity.goods.price }}
                              </div>
                              <div class="text-caption text-grey">
                                {{ commodity.goods.description }}
                              </div>
                            </q-card-section>
                          </q-card>
                          <q-separator />
                        </div>
                      </div>
                    </q-scroll-area>
                  </div>
                </q-tab-panel>

                <!--店铺信息展示-->
                <q-tab-panel name="Shops">
                  <div class="text-h6">Shops</div>
                  <div class="flex justify-center">
                    <q-scroll-area :visible="visible" style="height: 620px; width: 800px;" class="justify-center">
                      <div class="q-py-xs justify-around" style="height: 1000px; width: 800px;">
                        <div v-for="shop in shops" :key="shop">
                          <div style="height:250px">
                            <q-card class="shop-card" flat bordered>
                              <q-card-section>
                                <div class="text-overline text-orange-9">Overline</div>
                                <!-- <q-btn class="text-h5 q-mt-sm q-mb-xs" to="/singleShop">{{ shop.shopName }}</q-btn> -->
                                <q-btn flat class="text-h6" @click="toShop(shop.id)">
                                  {{ shop.shopName }}
                                </q-btn>
                                <div class="row no-wrap items-center">
                                  <q-rating size="18px" v-model="shop.stars" :max="5" color="primary" />
                                  <!-- <span class="text-caption text-grey q-ml-sm">4.2 (551)</span> -->
                                </div>
                                <q-separator />
                                <br>
                                <div class="text-caption text-grey">
                                  商品类别:
                                  <span flat v-for="c in shop.category" :key="c">
                                    {{ c }} &nbsp;

                                  </span>
                                  <br>
                                  商店简介: {{ shop.introduce }}
                                </div>
                              </q-card-section>

                              <q-card-actions>
                                <q-space />

                                <!-- <q-btn color="grey" round flat dense
                              :icon="expanded ? 'keyboard_arrow_up' : 'keyboard_arrow_down'"
                              @click="expanded = !expanded" /> -->
                              </q-card-actions>

                              <q-slide-transition>
                                <div v-show="expanded">
                                  <q-separator />
                                  <q-card-section class="text-subitle2">
                                    {{ shop.introduce }}
                                  </q-card-section>
                                </div>
                              </q-slide-transition>
                            </q-card>
                          </div>
                        </div>
                      </div>
                    </q-scroll-area>
                  </div>
                </q-tab-panel>
              </q-tab-panels>
            </div>

          </div>

          <div v-else class="q-pa-ma">
            <div>
              <q-tabs v-model="tab" dense class="text-grey-10" active-color="primary" indicator-color="primary"
                align="justify" narrow-indicator>
                <q-tab name="returnToAllCommodities" label="返回" @click="returnToAllCommos" />
                <q-tab name="Commodity" label="参加活动的商品" />
              </q-tabs>

              <q-separator />

              <q-tab-panels v-model="tab" animated>
                <q-tab-panel name="Commodity">
                  <!--商品信息展示---->
                  <div class="flex justify-center">
                    <q-scroll-area :visible="visible" style="height: 620px; width: 800px;" class="flex justify-center">
                      <div class="flex q-justify-around" style="width: 800px;">
                        <div v-if="sale_activity_commodities.length === 0" class="no-activity">
                          <div class="text-caption text-grey" style="text-align: center; margin-top: 10px;">暂时没有商品参加该活动
                          </div>
                        </div>
                        <div style="width:100%" v-else>
                          <div v-for="commodity in sale_activity_commodities" :key="commodity"
                            class="flex q-py-xs justify-around" style="width: 400px;">
                            <q-card class="my-card">
                              <div class="q-pa-md">
                                <q-carousel class="commodity_img" swipeable animated v-model="commodity.slide"
                                  height="200px" thumbnails infinite>
                                  <q-carousel-slide v-for="(image, index) in commodity.image" :key="index"
                                    :name="index + 1" :img-src="image" />
                                </q-carousel>
                              </div>

                              <q-card-section>
                                <div class="row no-wrap items-center">
                                  <q-btn flat class="text-h6" @click="toCommodity(commodity.id)">
                                    {{ commodity.goodsName }}
                                  </q-btn>
                                </div>

                                <q-rating v-model="commodity.stars" :max="5" size="32px" />
                              </q-card-section>

                              <q-card-section class="q-pt-none">
                                <div class="text-subtitle1" style="margin-top: 3%;">
                                  商品类型:
                                  <div class="text-subtitle1" v-for="category in commodity.category" :key="category"
                                    style="display: inline-block;">
                                    {{ category }} &nbsp;
                                  </div>
                                </div>
                                <div class="text-subtitle1">
                                  价格：¥{{ commodity.price }}
                                </div>
                                <div class="text-caption text-grey">
                                  {{ commodity.description }}
                                </div>
                              </q-card-section>
                            </q-card>
                            <q-separator />
                          </div>
                        </div>
                      </div>
                    </q-scroll-area>
                  </div>
                </q-tab-panel>

              </q-tab-panels>
            </div>

          </div>

          <!--购物车-->
          <div>
            <q-btn round color="purple" icon="shopping_cart" class="absolute" to="/userShoppingCart"
              style="top: 600px; right: 50px; transform: translateY(-50%);" size="30px">
            </q-btn>
          </div>

        </div>

        <div v-if="show_activity" style="width: 65%;">
          <div class="text-h5 q-mt-sm q-mb-xs">商城活动</div>
          <q-separator />

          <div v-if="all_activities.length === 0" class="no-activity">
            <div class="text-caption text-grey" style="text-align: center; margin-top: 10px;">暂时没有活动</div>
          </div>
          <div v-else class="display-activities">
            <div class="flex justify-center">
              <q-scroll-area :visible="visible" style="height: 620px; width: 100%;" class="flex justify-center">
                <div class="flex q-justify-around" style="width: 900px;">
                  <div v-for="activity in all_activities" :key="activity" class="flex q-py-xs justify-around"
                    style="width: 450px;">
                    <q-card class="my-card" flat bordered style="margin-top: 10px;">

                      <q-card-section>
                        <div class="text-overline text-orange-9">{{ activity.startDate }}至{{ activity.endDate }}</div>
                        <q-btn flat color="dark" class="text-h5" :label="activity.activityName"
                          style="margin-left: -15px;" @click="displayAllCommoditiesOfActivity(activity)" />
                        <br />
                        可参与活动的商品：
                        <div class="activity-category" v-for="category in activity.category" :key="category"
                          style="display: inline-block;">
                          {{ category }}&nbsp;
                        </div>
                      </q-card-section>

                    </q-card>
                  </div>
                </div>
              </q-scroll-area>
            </div>
          </div>

        </div>

      </div>


    </q-page-container>

  </q-layout>
</template>

<script setup>
import { ref, getCurrentInstance } from 'vue'
import axios from 'axios'
import { onMounted } from 'vue'
import { useRouter } from 'vue-router'

const url = ref('https://avatars.githubusercontent.com/u/105032850?s=400&u=285d7d130058e413bb8797cb52bc10f75c343076&v=4')
const username = localStorage.getItem('username');
const userId = localStorage.getItem('userId');
const tab = ref("Commodity")
const expanded = ref(false)
const loading = ref(false)
const router = useRouter()

let show_activity = ref(false)
let show_commodity = ref(true)

const axiosInstance = axios.create({
  baseURL: 'http://120.46.154.28:9999',
});
const shops = ref([])
const instance = getCurrentInstance()
const text = ref('')

const commodities = ref([])

let all_activities = ref([])
let display_activity_commos = ref(false)
let sale_activity_commodities = ref([])

function toCommodity(id) {
  console.log(id)

  localStorage.setItem('commodityId', id);

  router.push('/commodity');
}

//跳转到单个店铺
function toShop(id) {
  console.log(id)

  localStorage.setItem('shopId', id);

  router.push('/shop');
}

function search() {
  console.log(text.value)
  localStorage.setItem('text', text.value);
  router.push('/search');
}

onMounted(() => {
  //请求推荐商品
  console.log(localStorage.getItem('userId'))
  loading.value = true
  axiosInstance.get('/Goods/showRecommendGoods', {
    params: {
      userId: parseInt(userId)
    }
  }).then((response) => {
    commodities.value = []
    const r = response.data['data']
    console.log('请求所有商品信息：', r)
    r.forEach(function (item) {
      if (item !== null) {
        // console.log("商品信息", item)
        commodities.value.push(item)
      }
    });

    console.log('推荐商品：', commodities.value)
    loading.value = false
  });

  //请求所有已批准开店的店铺status=1
  axiosInstance.get('/shop/all').then((res) => {
    console.log('店铺')
    console.log(res.data.data)
    const r = res.data.data
    r.forEach(function (item) {
      if (item.status === 1 || item.status === 5 || item.status === 3) {
        shops.value.push(item)
      }
    });
    console.log('获取所有开店的店铺：', shops.value)

  });
  getAllSalesActivity()
  // displayAllCommoditiesOfActivity()
});

function getAllSalesActivity() {
  all_activities.value = []
  axiosInstance.get('/event/showEvent', {
    params: {}
  }).then((response) => {
    const r = response.data['data']
    console.log("get all activities: ", r)
    r.forEach(function (item) {
      if (item !== null && item.status === 1) {
        const [s_year, s_month, s_day] = item.startDate;
        item.startDate = `${s_year}年${s_month}月${s_day}日`;
        const [e_year, e_month, e_day] = item.endDate;
        item.endDate = `${e_year}年${e_month}月${e_day}日`;
        item.activityName = "满" + item.amount + "减" + item.discount
        all_activities.value.push(item)
      }
    });
  });
  console.log("获取所有活动：", all_activities.value)
}

function returnToAllCommos() {
  display_activity_commos.value = false
}

function displayAllCommoditiesOfActivity(activity) {
  sale_activity_commodities.value = []
  console.log("获取活动商品，活动id：", activity.id)
  axiosInstance.get(`/event/showGoods?eventId=${activity.id}`).then((response) => {
    const r = response.data['data']
    console.log("活动商品: ", r)
    r.forEach(function (item) {
      if (item !== null) {
        sale_activity_commodities.value.push(item)
      }
    });
  }).then((response) => {
    console.log("请求参加某活动的所有商品: ", response)
    show_activity.value = false
    show_commodity.value = true
    display_activity_commos.value = true
  });
}

</script>

<style lang="sass" scoped>
.my-card
  width: 100%
  max-width: 350px
  height: 100%
  max-height: 430px

.shop-card
  width: 100%
  width: 700px
  height: 100%
  max-height: 210px
</style>
