<template>
  <q-layout view="hHh lpR fFf">

    <q-header elevated class="bg-primary text-white">
      <q-toolbar>
        <q-btn dense flat round icon="menu" @click="toggleLeftDrawer" />

        <q-toolbar-title>
          <q-avatar>
            <img src="https://cdn.quasar.dev/logo-v2/svg/logo-mono-white.svg">
          </q-avatar>
          搜索结果
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

            <q-separator />

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

        <!--所有商品以及店铺信息展示-->
        <div class="q-pa-ma" style="max-width: 75%; ">
          <div>
            <q-tabs v-model="tab" dense class="text-grey-10" active-color="primary" indicator-color="primary"
              align="justify" narrow-indicator>
              <q-tab name="Commodity" label="搜索结果" />
            </q-tabs>

            <q-separator />

            <q-tab-panels v-model="tab" animated>
              <q-tab-panel name="Commodity">
                <!--商品信息展示---->
                <div class="text-h6">Commodity</div>
                <div v-if="loading">
                  <q-spinner-comment color="deep-purple" size="5.5em" />
                </div>
                <div v-else>
                  <div class="flex justify-center">
                    <q-scroll-area :visible="visible" style="height: 620px; width: 800px;" class="flex justify-center">
                      <div class="flex q-justify-around" style="width: 800px;">
                        <div v-for="commodity in commodities" :key="commodity" class="flex q-py-xs justify-around"
                          style="width: 400px;">
                          <q-card class="my-card">
                            <div class="q-pa-md">
                              <q-carousel class="commodity_img" swipeable animated v-model="commodity.slide"
                                height="200px" thumbnails infinite>
                                <q-carousel-slide v-for="(image, index) in commodity.goods.image" :key="index"
                                  :name="index + 1" :img-src="image" />
                              </q-carousel>
                            </div>

                            <q-card-section>
                              <div class="row no-wrap items-center">
                                <q-btn flat class="text-h6" @click="toCommodity(commodity.goods.id)">
                                  {{ commodity.goods.goodsName }}
                                </q-btn>
                                <q-chip v-if="commodity.event.id != 0 && commodity.event.status == 1" outline color="orange"
                                  text-color="white" icon-right="star">
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
const router = useRouter()
const text = ref(localStorage.getItem('text'))
const loading = ref(false)

const axiosInstance = axios.create({
  baseURL: 'http://120.46.154.28:9999',
});
const instance = getCurrentInstance()

const commodities = ref([])

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
  console.log("搜索")
  localStorage.setItem('text', text.value);

  commodities.value = []
  let r1, r2;
  loading.value = true;

  axiosInstance.get('/Goods/searchGoods', {
    params: {
      keyword: text.value
    }
  }).then((response) => {
    commodities.value = []
    r1 = response.data['data']
    console.log("r1: ", r1);
    instance.proxy.$forceUpdate();
    axiosInstance.get('/Goods/searchGoodsCategory', {
      params: {
        keyword: text.value
      }
    }).then((response) => {
      r2 = response.data['data']
      console.log("r2: ", r2);
      for(let i=0; i<r2.length; i++){
        let flag = 0;
        for(let j=0; j<r1.length; j++){
          if(r2[i].goods.id == r1[j].goods.id){
            flag = 1;
            break;
          }
        }
        if(flag == 0){
          r1.push(r2[i]);
        }
      }
      commodities.value = r1;
      console.log("r: ", commodities  );
      loading.value = false
      if (commodities.value.length == 0) {
        alert("没有搜索到相关商品")
      }
    });

  });

}

onMounted(() => {
  console.log("localStorage")
  console.log(localStorage.getItem('userId'))
  let r1, r2;
  commodities.value = []

  //请求搜索结果
  axiosInstance.get('/Goods/searchGoods', {
    params: {
      keyword: localStorage.getItem('text')
    }
  }).then((response) => {
    commodities.value = []
    r1 = response.data['data']
    instance.proxy.$forceUpdate();
    console.log("r1: ", r1);
    axiosInstance.get('/Goods/searchGoodsCategory', {
      params: {
        keyword: text.value
      }
    }).then((response) => {
      r2 = response.data['data']
      console.log("r2: ", r2);
      for(let i=0; i<r2.length; i++){
        let flag = 0;
        for(let j=0; j<r1.length; j++){
          if(r2[i].goods.id == r1[j].goods.id){
            flag = 1;
            break;
          }
        }
        if(flag == 0){
          r1.push(r2[i]);
        }
      }
      commodities.value = r1;
      console.log("r: ", commodities  );
      loading.value = false
      if (commodities.value.length == 0) {
        alert("没有搜索到相关商品")
      }
    });
  });

});

function returnToAllCommos() {
  display_activity_commos.value = false
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
