<template>
  <div class="bg" style="height: 100%;">
    <q-layout view="hHh lpR fFf">

      <q-page-container>
        <router-view />
        <!--商店信息展示-->
        <br />
        <div style="width:80%; margin: auto">
          <q-card class="my-card" flat bordered style="height:auto">

            <q-item>
              <q-item-section avatar>
                <q-avatar size="70px">
                  <img
                    src='https://avatars.githubusercontent.com/u/105032850?s=400&u=285d7d130058e413bb8797cb52bc10f75c343076&v=4'>
                </q-avatar>
              </q-item-section>

              <q-item-section>
                <div class="text-h5 q-mt-sm q-mb-xs">{{ shopName }}</div>
              </q-item-section>

              <q-item-section>
                <q-btn flat style="color: #FF0080; width:30%; margin-left: 65%;" label="进入店铺" @click="toShop()" />
              </q-item-section>
            </q-item>
          </q-card>
        </div>
        <br />

        <!--商品信息展示-->
        <div style="width:80%; margin: auto;">

          <q-card style="width:100%; height:auto">
            <div>
              <q-dialog v-model="dialogVisible">
                <q-img :src="currentSlideSrc" />
              </q-dialog>
            </div>

            <div class="row flex">

              <div class="q-pa-md" style="width:40%">
                <q-carousel swipeable animated arrows thumbnails v-model="slide" infinite
                  style="width:100%; height:300px; margin-left: 5%;">
                  <q-carousel-slide v-for="(img, index) in image" :key="index" :name="index + 1" :img-src="img"
                    @click="showDialog(index + 1)" />
                </q-carousel>
              </div>

              <div class="row" style="width:60%">
                <div style="width:50%">
                  <br />
                  <div style="width:100%; margin-left: 10%;">
                    <div style="margin-top: 3%;" class="text-h5">{{ commodityName }}</div>
                    <div style="margin-top:1%" class="caption text-grey"> 月销{{ commoditySales }}</div>

                    <div class="row">
                      <div class="text-red text-h4" style="margin-top:2%">¥ {{ commodityPrice }}</div>
                      <q-chip v-if="commodityEvent.status ==1" outline style="margin-top: 3%;" color="orange" text-color="white" icon-right="star">
                        满{{ commodityEvent.amount }}减{{ commodityEvent.discount }}
                      </q-chip>
                    </div>

                    <br />
                    <div class="row">
                      <div class="text-grey">发货地</div>
                      <div style="margin-left: 2%;">{{ shopAddress }} </div>
                    </div>
                    <br />

                    <div class="row">
                      <div class="text-grey">数量</div>
                      <div class="flex" style="margin-left: 2%;">
                        <q-btn class="decrement" icon="remove" size="xs" @click="decreaseQuantity" />
                        <div class="justify-center" style="width:30px; display:flex; align-items:center;">
                          {{ commodityQuantity }}</div>
                        <q-btn class="increment" icon="add" size="xs" @click="commodityQuantity++" />
                      </div>
                    </div>

                    <br />
                    <div class="row">
                      <div style="margin-top: 10%;">
                        <q-btn outline style="color: red;" label="立即购买" @click="toOrder()" />
                      </div>
                      <div style="margin-top: 10%; margin-left: 8%;">
                        <q-btn outline style="color: orange;" label="加入购物车" @click="addShoppingCart()"  />
                      </div>
                    </div>

                  </div>

                  <div>

                  </div>
                </div>
                <div style="width:50%">
                  <q-img :src="imglist[2]" />
                </div>
              </div>
            </div>

            <div class="row">
              <div style="margin-left: 5%; width:40%">
                <div class="row">
                  <q-chip outline color="orange" text-color="white" icon-right="star">
                    Star
                  </q-chip>
                  <q-rating v-model="stars" :max="5" size="32px" />
                </div>

                <div style="margin-top: 2%; width:70%">
                  <q-chip style="width:96%;" outline color="teal" text-color="white" icon="bookmark">
                    description
                  </q-chip>
                  <div class="text-grey">{{ commodityDescription }}</div>
                </div>

              </div>


              <div class="row" style="width:55%;position: relative;">
                <div>
                  <q-btn round color="info" icon="shopping_cart" class="absolute" to="/userShoppingCart"
                    style="right: 43%;z-index: 2;" size="30px">
                  </q-btn>
                </div>
                <div style="width:50%; z-index: 1; margin-left: 42%;">
                  <q-img :src="imglist[1]" />
                </div>
              </div>
            </div>
          </q-card>
        </div>

        <div>
          <q-dialog v-model="layout">
            <q-card style="width:200px; height:120px">
              <q-card-section>
                <div class="text-h5">加入购物车成功</div>
              </q-card-section>
              <q-card-actions>
                <q-btn style="left:30%" color="primary" icon="check" label="确定" @click="check()" />
              </q-card-actions>
            </q-card>
          </q-dialog>
        </div>
      </q-page-container>

    </q-layout>
  </div>
</template>
<script setup>
import { ref, computed } from 'vue'
import axios from 'axios'
import { onMounted } from 'vue'
import chilling from '../../assets/imgs/chilling.png'
import sprinting from '../../assets/imgs/sprinting.png'
import star from '../../assets/imgs/star.png'
import { useRouter } from 'vue-router'

const imglist = ref([sprinting, chilling, star])
const commodity = ref({})
const shop = ref({})
const stars = ref(4)
const router = useRouter()
const layout = ref(false)

const axiosInstance = axios.create({
  baseURL: 'http://120.46.154.28:9999',
});

const commodityQuantity = ref(1)

const commodityName = computed(() => {
  return commodity.value?.goods?.goodsName || '';
});

const commodityPrice = computed(() => {
  return commodity.value?.goods?.price || '';
});

const commoditySales = computed(() => {
  return commodity.value?.goods?.sales;
});

const commodityDescription = computed(() => {
  return commodity.value?.goods?.description || '';
});

const commodityEvent = computed(() => {
  return commodity.value?.event || '';
});

const shopName = computed(() => {
  return shop.value?.shopName || '';
});

const shopAddress = computed(() => {
  return shop.value?.address || '';
});

const image = computed(() => {
  return commodity.value?.goods?.image || [];
});

const addressOption = computed(() => {
  return options.value && options.value.length > 0 ? options.value : ['暂无收货地址'];
});

const options = ref([
  '上海市 杨浦区', '安徽省 淮南市寿县 现代汉城21楼 一单元101', 'Twitter', 'Apple', 'Oracle'
])

const slide = ref(1)
const dialogVisible = ref(false)
const currentSlide = ref(1)
const address = ref('')
const commodityId = localStorage.getItem('commodityId')

function showDialog(slideNumber) {
  currentSlide.value = slideNumber
  dialogVisible.value = true
}

function decreaseQuantity() {
  if (commodityQuantity.value > 1) {
    commodityQuantity.value--
  }
}

const currentSlideSrc = computed(() => {
  return image.value[currentSlide.value - 1]
})

//跳转到单个店铺
function toShop() {

  localStorage.setItem('shopId', commodity.value.goods.shopId);

  router.push('/shop');
}

function toOrder(){
  console.log('toOrder')
  const result_list = []
  //未优惠价格
  const total = commodity.value.goods.price * commodityQuantity.value
  //优惠价格
  let multiple = 0
  if(commodity.value.event.id)
  {
    multiple.value = Math.floor(total / commodity.value.event.amount);
  }
  const total_event = total - multiple*commodity.value.event.discount;
  const total_discount = total - total_event;

  localStorage.setItem('total', total)
  localStorage.setItem('total_event', total_event)
  localStorage.setItem('total_discount', total_discount)
  console.log('total')
  console.log(total)
  console.log('total_event')
  console.log(total_event)
  console.log('total_discount')
  console.log(total_discount)

  const unitPrice = parseFloat((total_event / commodityQuantity.value).toFixed(2))
  console.log('unitPrice')
  console.log(unitPrice)

  let obj = {
    "eventId": commodity.value.goods.eventId,
    "goodsId": commodity.value.goods.id,
    "quantity": commodityQuantity.value,
    "shopId": commodity.value.goods.shopId,
    "source": false,
    "totalPrice": total_event,
    "unitPrice": unitPrice,
    "userId": localStorage.getItem('userId')
  }
  console.log('obj')
  console.log(obj)

  result_list.push(obj)
  const event_detail = {}
  commodity.value.event['event_price'] = total
  commodity.value.event['event_price_after'] = total_event
  console.log('commodity.value.event')
  console.log(commodity.value)
  let list =[]
  list.push(commodity.value)
  console.log('list')
  console.log(list)

  if(commodity.value.event.id == 0 || commodity.value.event.status!=1){
    event_detail['0'] = []
  }
  else{
    event_detail[commodity.value.event.id] = list
  }
  console.log('result_list')
  console.log(result_list)

  localStorage.setItem('event_detail', JSON.stringify(event_detail))
  axiosInstance.post('/userOrderTemplate/order', result_list).then((res) => {
    console.log(res.data)
    const code = res.data['code']
    localStorage.setItem('orderTemplate', res.data['data'])
    if(code == 20000){
      router.push('/order')
    }
  });
}

function addShoppingCart(){
  console.log('shoppingCart')
  axiosInstance.post(`/cart/addMulti2cart?goodsId=${commodityId}&userId=${localStorage.getItem('userId')}&quantity=${commodityQuantity.value}`).then((res) => {
    console.log("addShoppingCart")
    console.log(res.data)
    const code = res.data['code']
    if(code == 20000){
      layout.value = true
    }
  });
}

function check(){
  layout.value = false
  commodityQuantity.value = 1
}

onMounted(async () => {

  //用单个商品的id获取商品信息
  console.log('commodityId')
  console.log(commodityId)

  axiosInstance.get('/Goods/GoodsDetails', {
    params: {
      goodsId: commodityId
    }
  }).then((response) => {
    const r = response.data['data']
    console.log('commodity message')
    console.log(r)
    commodity.value = r
    console.log(commodity.value.goods.goodsName)

    axiosInstance.get('/shop/showShopByShopId', {
      params: {
        shopId: commodity.value.goods.shopId
      }
    }).then((response) => {
      const r = response.data['data']
      console.log('shop message')
      shop.value = r;
    });
  });


})
</script>

<style scoped>
.bg {
  background-image: linear-gradient(to right top, #d16ba5, #c777b9, #ba83ca, #aa8fd8, #9a9ae1, #909ae6, #849aec, #769af1, #708ef7, #6f80fb, #7571fc, #7f5ffb);
}
</style>
