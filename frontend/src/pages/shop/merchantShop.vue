<template>
  <q-layout view="hHh lpR fFf">

    <q-header elevated class="bg-primary text-white">
      <q-toolbar>
        <q-btn dense flat round icon="menu" @click="toggleLeftDrawer" />

        <q-toolbar-title>

          <q-avatar>
            <img src="https://cdn.quasar.dev/logo-v2/svg/logo-mono-white.svg">
          </q-avatar>
          {{ shopName }}
        </q-toolbar-title>
        <q-btn flat round dense icon="update" class="q-mr-xs" @click="update"></q-btn>

      </q-toolbar>
    </q-header>

    <q-drawer v-model="leftDrawerOpen" :width="280">
      <q-scroll-area class="fit">
        <q-list padding class="text-grey-8" style="margin-top: 3%;">
          <q-item class="GNL__drawer-item" v-ripple v-for="link in links1" :key="link.text" clickable
            @click="handleClick(link)" style="margin-top: 3%;">
            <q-item-section avatar>
              <q-icon :name="link.icon" />
            </q-item-section>
            <q-item-section>
              <q-item-label>{{ link.text }}</q-item-label>
            </q-item-section>
          </q-item>

          <q-separator inset class="q-my-sm" style="margin-top: 4%;" />

          <q-item class="GNL__drawer-item" v-ripple v-for="link in links2" :key="link.text" clickable
            @click="handleClick(link)" style="margin-top: 3%;">
            <q-item-section avatar>
              <q-icon :name="link.icon" />
            </q-item-section>
            <q-item-section>
              <q-item-label>{{ link.text }}</q-item-label>
            </q-item-section>
          </q-item>

          <q-item class="GNL__drawer-item" :to="isMerchant ? '/merchantpage' : '/user'" exact style="margin-top: 3%;">
            <q-item-section avatar>
              <q-icon name="font_download" />
            </q-item-section>

            <q-item-section>
              个人主页
            </q-item-section>
          </q-item>

          <!-- 待写：帮助页面 -->
          <q-item class="GNL__drawer-item" to="/help" exact style="margin-top: 3%;">
            <q-item-section avatar>
              <q-icon name="open_in_new" />
            </q-item-section>

            <!-- TODO: 完善帮助页面 -->
            <q-item-section>
              帮助
            </q-item-section>
          </q-item>

          <q-item class="GNL__drawer-item" to="/contact" exact style="margin-top: 3%;">
            <q-item-section avatar>
              <q-icon name="send" />
            </q-item-section>

            <!-- TODO: 完善联系页面 -->
            <q-item-section>
              联系我们
            </q-item-section>
          </q-item>

          <q-separator inset class="q-my-sm" style="margin-top: 3%;" />

          <div class="q-mt-md" style="margin-top: 6%;">
            <div class="flex flex-center q-gutter-xs">
              <a class="GNL__drawer-footer-link" href="javascript:void(0)" aria-label="Privacy">Privacy</a>
              <span> · </span>
              <a class="GNL__drawer-footer-link" href="javascript:void(0)" aria-label="Terms">Terms</a>
            </div>
            <div class="logoutLink flex flex-center q-gutter-xs" style="margin-top: 3%;">
              <a class="GNL__drawer-footer-link" href='/#/login' style="font-weight: bold;">Logout</a>

            </div>
          </div>
        </q-list>
      </q-scroll-area>
    </q-drawer>

    <q-page-container>
      <router-view />
      <div class="q-pa-md row items-start q-gutter-md" style="width: 100%;">

        <!-- 左侧栏的店铺信息展示 -->
        <div class="left-side-display" style="display: flex; width: 30%; margin-top: 3%; flex-direction: column;">

          <!-- 店铺详情展示 -->
          <div class="shopInfoDisplay">
            <q-card class="my-card" flat bordered>
              <q-card-section>
                <div class="text-overline text-orange-9">注册时间：{{ shopRegistrationTime }}</div>
                <div class="text-h5 q-mt-sm q-mb-xs">店铺名称：{{ shopName }}</div>
                <div class="text-overline text-grey "><q-icon name="place" />
                  {{ shopAddress }}</div>
                <div class="text-caption text-grey">简介：{{ shopIntroduce }}</div>
                <!-- <div class="text-caption text-grey">售卖商品：{{ shopCategory }}</div> -->
                <div class="text-caption text-grey" style="margin-top: 3%;">
                  售卖商品:
                  <div class="text-caption text-grey" v-for="category in shopCategory" :key="category"
                    style="display: inline-block;">
                    {{ category }} &nbsp;
                  </div>
                </div>
              </q-card-section>

              <q-card-actions>
                <q-btn flat color="secondary" label="查看商品" style="margin-left: 0.5%;"
                  @click="show_transition = false; show_commodity = true; show_activity = false; show_order = false; " />
                <q-btn flat color="secondary" label="查看流水" style="margin-left: 0.5%;"
                  @click="show_transition = true; show_commodity = false; show_activity = false; show_order = false; " />
                <q-btn flat color="secondary" label="查看商城活动" style="margin-left: 0.5%;"
                  @click="show_transition = false; show_commodity = false; show_activity = true; show_order = false; " />
                <q-btn flat color="secondary" label="查看订单" style="margin-left: 0.5%;"
                  @click="show_transition = false; show_commodity = false; show_activity = false; show_order = true; " />
              </q-card-actions>

            </q-card>
          </div>
        </div>

        <!-- 展示商店商品 -->
        <div v-if="show_commodity" style="width: 65%;">
          <!--商店商品展示-->
          <div>
            <q-tabs v-model="tab" dense class="text-grey" active-color="primary" indicator-color="primary" align="justify"
              narrow-indicator>
              <q-tab name="onShelve" label="在售商品" />
              <q-tab name="applications" label="待审批&失败的申请记录" />
              <q-tab name="removals" label="已下架商品" />

            </q-tabs>

            <q-separator />

            <q-tab-panels v-model="tab" animated>
              <q-tab-panel name="onShelve">
                <div class="flex justify-center">
                  <q-scroll-area :visible="visible" style="height: 620px; width: 100%;" class="flex justify-center">
                    <div class="flex q-justify-around" style="width: 900px;">
                      <div v-for="commodity in onShelveCmdt" :key="commodity" class="flex q-py-xs justify-around"
                        style="width: 450px;">
                        <q-card class="my-card">
                          <div class="q-pa-md">
                            <q-carousel class="commodity_img" swipeable animated v-model="commodity.slide" height="200px"
                              thumbnails infinite>
                              <q-carousel-slide v-for="(image, index) in commodity.image" :key="index" :name="index + 1"
                                :img-src="image" />
                            </q-carousel>
                          </div>

                          <q-card-section>
                            <q-btn fab color="primary" icon="delete" class="absolute"
                              style="top: 0; right: 12px; transform: translateY(-50%);"
                              @click="confirmDeleteCommodity(commodity)" />

                            <div class="row no-wrap items-center">
                              <div class="col text-h6 ellipsis">
                                <q-btn flat class="text-h6" @click="toEditCommodity(commodity)">
                                  {{ commodity.goodsName }}
                                </q-btn>
                              </div>
                            </div>

                            <q-separator />
                            <!-- <q-rating v-model="stars" :max="5" size="32px" /> -->
                            <div class="text-subtitle3" style="margin-top: 3%;">
                              状态: {{ commodity.status }}
                            </div>
                            <div class="text-subtitle1" style="margin-top: 3%;">
                              商品类型:
                              <div class="text-subtitle1" v-for="category in commodity.category" :key="category"
                                style="display: inline-block;">
                                {{ category }} &nbsp;
                              </div>
                            </div>
                            <div class="text-subtitle1">
                              ¥ price: {{ commodity.price }}
                            </div>
                            <div class="text-caption text-grey">
                              {{ commodity.description }}
                            </div>
                          </q-card-section>
                        </q-card>
                        <q-separator />

                      </div>

                    </div>
                  </q-scroll-area>
                </div>
              </q-tab-panel>

              <q-tab-panel name="applications">
                <div class="flex justify-center">
                  <q-scroll-area :visible="visible" style="height: 620px; width: 100%;" class="flex justify-center">
                    <div class="flex q-justify-around" style="width: 900px;">
                      <div v-for="commodity in applyingCmdt" :key="commodity" class="flex q-py-xs justify-around"
                        style="width: 450px;">
                        <q-card class="my-card">
                          <div class="q-pa-md">
                            <q-carousel class="commodity_img" swipeable animated v-model="commodity.slide" height="200px"
                              thumbnails infinite>
                              <q-carousel-slide v-for="(image, index) in commodity.image" :key="index" :name="index + 1"
                                :img-src="image" />
                            </q-carousel>
                          </div>

                          <q-card-section>
                            <!-- <q-btn fab color="primary" icon="delete" class="absolute"
                              style="top: 0; right: 12px; transform: translateY(-50%);"
                              @click="deleteCommodity(commodity)" /> -->

                            <div class="row no-wrap items-center">
                              <div class="col text-h6 ellipsis">
                                <q-btn flat class="text-h6">
                                  {{ commodity.goodsName }}
                                </q-btn>
                              </div>
                              <!-- <div class="col-auto text-grey text-caption q-pt-md row no-wrap items-center">
                                下架商品
                              </div> -->
                            </div>

                            <q-separator />
                            <!-- <q-rating v-model="stars" :max="5" size="32px" /> -->
                            <div class="text-subtitle3" style="margin-top: 3%;">
                              状态: {{ commodity.status }}
                            </div>
                            <div class="text-subtitle1" style="margin-top: 3%;">
                              商品类型:
                              <div class="text-subtitle1" v-for="category in commodity.category" :key="category"
                                style="display: inline-block;">
                                {{ category }} &nbsp;
                              </div>
                            </div>
                            <div class="text-subtitle1">
                              ¥ price: {{ commodity.price }}
                            </div>
                            <div class="text-caption text-grey">
                              {{ commodity.description }}
                            </div>
                          </q-card-section>
                        </q-card>
                        <q-separator />

                      </div>
                    </div>
                  </q-scroll-area>
                </div>
              </q-tab-panel>

              <q-tab-panel name="removals">
                <div class="flex justify-center">
                  <q-scroll-area :visible="visible" style="height: 620px; width: 100%;" class="flex justify-center">
                    <div class="flex q-justify-around" style="width: 900px;">
                      <div v-for="commodity in removedCmdt" :key="commodity" class="flex q-py-xs justify-around"
                        style="width: 450px;">
                        <q-card class="my-card">
                          <div class="q-pa-md">
                            <q-carousel class="commodity_img" swipeable animated v-model="commodity.slide" height="200px"
                              thumbnails infinite>
                              <q-carousel-slide v-for="(image, index) in commodity.image" :key="index" :name="index + 1"
                                :img-src="image" />
                            </q-carousel>
                          </div>

                          <q-card-section>
                            <!-- <q-btn fab color="primary" icon="delete" class="absolute"
                              style="top: 0; right: 12px; transform: translateY(-50%);"
                              @click="deleteCommodity(commodity)" /> -->

                            <div class="row no-wrap items-center">
                              <div class="col text-h6 ellipsis">
                                <q-btn flat class="text-h6">
                                  {{ commodity.goodsName }}
                                </q-btn>
                              </div>
                              <!-- <div class="col-auto text-grey text-caption q-pt-md row no-wrap items-center">
                                下架商品
                              </div> -->
                            </div>

                            <q-separator />
                            <!-- <q-rating v-model="stars" :max="5" size="32px" /> -->
                            <div class="text-subtitle3" style="margin-top: 3%;">
                              状态: {{ commodity.status }}
                            </div>
                            <div class="text-subtitle1" style="margin-top: 3%;">
                              商品类型:
                              <div class="text-subtitle1" v-for="category in commodity.category" :key="category"
                                style="display: inline-block;">
                                {{ category }} &nbsp;
                              </div>
                            </div>
                            <div class="text-subtitle1">
                              ¥ price: {{ commodity.price }}
                            </div>
                            <div class="text-caption text-grey">
                              {{ commodity.description }}
                            </div>
                          </q-card-section>
                        </q-card>
                        <q-separator />

                      </div>
                    </div>
                  </q-scroll-area>
                </div>
              </q-tab-panel>

            </q-tab-panels>

            <!--上架商品-->
            <div>
              <q-btn round color="purple" icon="add" class="absolute" @click="applyAddCommodity()"
                style="top: 600px; right: 50px; transform: translateY(-50%);" size="30px">
              </q-btn>
              <q-dialog v-model="layout" no-click-outside-close>
                <q-layout view="Lhh lpR fff" container class="bg-white">
                  <q-header class="bg-primary">
                    <q-toolbar>
                      <q-btn flat @click="drawer = !drawer" round dense icon="menu" />
                      <q-toolbar-title>申请上架商品</q-toolbar-title>
                      <q-btn flat @click="drawerR = !drawerR" round dense icon="menu" />
                      <q-btn flat v-close-popup round dense icon="close" @click="layout = false" />
                    </q-toolbar>
                  </q-header>

                  <q-footer class="bg-black text-white">
                    <q-toolbar inset>
                      <q-toolbar-title>Apply</q-toolbar-title>
                    </q-toolbar>
                  </q-footer>

                  <q-page-container>
                    <q-page padding>
                      <q-form ref="myForm" @submit="onSubmit" @reset="onReset" class="q-gutter-md">
                        <br>
                        <q-input filled v-model="goodsName" label="商品名称 *" hint="Name and surname" lazy-rules
                          :rules="[val => val && val.length > 0 && val.length < 13 || '请输入您的商品名称']" />

                        <div class="add-commodity-category" style="margin-left: 2%;">
                          <div class="title" color="grey-9" style="font-size: larger; margin-left: 2%;">
                            商品类别：
                          </div>
                          <div class="category-choices" v-for="category in shopCategory" :key="category">
                            <q-radio v-model="new_commodity_category" checked-icon="task_alt"
                              unchecked-icon="panorama_fish_eye" :label="category" :val="category"
                              style="margin-left: 2%;" />
                          </div>
                        </div>

                        <q-input filled v-model="description" label="商品简介 *"
                          hint="Introduce your store within 128 characters" lazy-rules :rules="[val => val && val.length > 0 || '请输入您的简介',
                          val => val.length < 129 || '超过字数限制']">
                        </q-input>

                        <q-input filled v-model="price" label="商品价格 *" hint="registered funds" lazy-rules :rules="[val => val && val.length > 0 || '请输入商品价格',
                        checkprice || '价格需为大于0的小数']"></q-input>

                        <q-uploader field-name="files" style="width: 530px" url="http://120.46.154.28:9999/Goods/uploadImg"
                          label="Restricted to images" multiple @uploading="onFileUploading" accept="image/*"
                          Content-Type='multipart/form-data' :auto-upload="false" @uploaded="handleUpload($event)"
                          @rejected="onRejected" />

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

          </div>

        </div>

        <!-- 下架商品确认 -->
        <div>
          <q-dialog v-model="layout_delete" no-click-outside-close>
            <q-layout view="Lhh lpR fff" container class="bg-white">
              <q-header class="bg-primary">
                <q-toolbar>
                  <q-btn flat @click="drawer = !drawer" round dense icon="menu" />
                  <q-toolbar-title>下架商品</q-toolbar-title>
                  <q-btn flat @click="drawerR = !drawerR" round dense icon="menu" />
                  <q-btn flat v-close-popup round dense icon="close" @click="layout_delete = false" />
                </q-toolbar>
              </q-header>

              <q-footer class="bg-black text-white">
                <q-toolbar inset>
                  <q-toolbar-title>Apply</q-toolbar-title>
                </q-toolbar>
              </q-footer>

              <q-page-container>
                <q-page padding style="height:100px">
                  <div class="text-h5 q-mt-sm q-mb-xs" style="height:100px"> <q-chip size="lg" icon="bookmark">
                      商品名称
                    </q-chip> {{ delete_goodsName }}</div>
                  <div class="text-h5 q-mt-sm q-mb-xs" style="height:100px"> <q-chip size="lg" icon="bookmark">
                      商品简介
                    </q-chip> {{ delete_description }}</div>
                  <div class="text-h5 q-mt-sm q-mb-xs" style="height:120px"> <q-chip size="lg" icon="bookmark">
                      商品价格
                    </q-chip> ¥ {{ delete_price }}</div>
                  <q-btn color="primary" glossy label="Delete" @click="deleteOneCommodity" />
                </q-page>
              </q-page-container>
            </q-layout>
          </q-dialog>
        </div>

        <!-- 展示商店流水 -->
        <div v-if="show_transition" style="width: 65%;">
          <div class="text-h5 q-mt-sm q-mb-xs">店铺收支</div>
          <q-separator />

          <div class="transaction-option-box" style="margin-left: 1%; margin-top: 1%;">
            <q-radio v-model="transaction_range" checked-icon="task_alt" unchecked-icon="panorama_fish_eye"
              val="all_transactions" label="所有记录" @update:model-value="getTransfer" />
            <q-radio v-model="transaction_range" checked-icon="task_alt" unchecked-icon="panorama_fish_eye"
              val="last_month_transactions" label="近一个月的记录" @update:model-value="getTransfer" />
            <q-radio v-model="transaction_range" checked-icon="task_alt" unchecked-icon="panorama_fish_eye"
              val="last_week_transactions" label="近一周的记录" @update:model-value="getTransfer" />
          </div>
          <q-table :rows="transitionRows" :columns="transitionColumn" row-key="transferName"
            style="margin-top: 2%; margin-left: 3%; width: 90%; " />
        </div>

        <!-- 商城活动展示 -->
        <div v-if="show_activity" style="width: 65%;">
          <div class="text-h5 q-mt-sm q-mb-xs">商城活动</div>
          <q-separator />

          <div v-if="all_activities.length === 0" class="no-activity">
            <div class="text-caption text-grey" style="text-align: center; margin-top: 10px;">暂时没有活动</div>
          </div>
          <div v-else>
            <div class="flex justify-center">
              <q-scroll-area :visible="visible" style="height: 620px; width: 100%;" class="flex justify-center">
                <div class="flex q-justify-around" style="width: 900px;">
                  <div v-for="activity in all_activities" :key="activity" class="flex q-py-xs justify-around"
                    style="width: 450px;">
                    <q-card class="my-card" flat bordered style="margin-top: 10px;">
                      <q-card-section>
                        <div class="text-overline text-orange-9">起始日期</div>
                        <div class="text-h5 q-mt-sm q-mb-xs">满{{ activity.amount }}减{{ activity.discount }}</div>
                        活动时间：{{ activity.startDate }}至{{ activity.endDate }}。<br />
                        可参与活动的商品：
                        <div class="activity-category" v-for="category in activity.category" :key="category"
                          style="display: inline-block;">
                          {{ category }}&nbsp;
                        </div>
                      </q-card-section>

                      <q-card-actions>
                        <div class="btn-box" style="width: 100%;">
                          <q-btn flat color="secondary" label="申请参加" style="float: right;"
                            @click="applyForActivity(activity)" />
                        </div>
                      </q-card-actions>

                    </q-card>
                  </div>
                </div>
              </q-scroll-area>
            </div>
          </div>

        </div>

        <!-- 订单展示 -->
        <div v-if="show_order" style="width: 65%;">
          <div class="btn-col" style="width: 100%; display: flex;">
            <q-btn flat color="secondary" style="flex-grow: 1;" label="未发货订单" @click="showShopUnshippedOrder" />
            <q-btn flat color="secondary" style="flex-grow: 1;" label="退款订单" @click="showShopRefundingOrder" />
          </div>
          <div v-if="shop_unshipped_order" style="margin-top: 3%;">
            <div v-if="shop_unshipped_order_lst.length === 0">
              <div class="text-caption text-grey" style="text-align: center; margin-top: 10px;">暂时没有未发货的订单哦</div>
            </div>
            <div v-else>
              <div v-for="order in shop_unshipped_order_lst" :key="order">
                <q-card class="shop-card" flat bordered style="width: 95%; margin-top: 2%; display: flex;">
                  <q-card-section horizontal style="flex-grow: 9.5; width: 85%;">
                    <q-card-section class="q-pt-xs" style="width: 100%; display: flex;">
                      <div class="customer-order-text" style="width: 100%; flex-grow: 5;">
                        <div class="text-overline text-grey-10">订单编号{{ order.userOrder.id }}</div>

                        <div class="q-mt-sm q-mb-xs" style="font-size: large;">商店名称{{
                          order.shop.shopName }}</div>
                        <div class="text-bold q-mt-sm q-mb-xs" style="font-size: large;">商品名称{{
                          order.goods.goodsName }}</div>
                        <div class="text-bold q-mt-sm q-mb-xs" style="margin-left: 3px; color: gray;">×
                          {{ order.userOrder.quantity }}</div>
                        <div class="text-bold q-mt-sm q-mb-xs">实付款：¥{{ order.userOrder.totalPrice }}</div>

                        <div class="text q-mt-sm q-mb-xs">收货人：{{ order.deliveryAddress.name }}&nbsp; &nbsp; 联系电话：{{
                          order.deliveryAddress.phoneNumber }}</div>
                        <div class="text q-mt-sm q-mb-xs">收货地址：{{ order.deliveryAddress.address }}</div>

                      </div>

                      <div class="customer-order-pict" style=" flex-grow: 5;">
                        <!-- TODO: 填上商品图片 -->
                        <q-img class="rounded-borders" :src="order.goods.image[0]"
                          style="width:200px; height:150px"></q-img>
                      </div>
                    </q-card-section>
                  </q-card-section>

                  <q-card-actions style="flex-grow: 0.5;">
                    <!-- TODO: 补充操作 -->
                    <q-btn flat style="color: #570089; font-size: large;" label="发货"
                      @click="shopShipOrder(order.userOrder.id)" />
                  </q-card-actions>
                </q-card>
              </div>
            </div>

            <div class="btn-box" style="display: flex;">
              <q-btn flat color="secondary" label="上一页" style="flex-grow: 1;"
                @click="shop_unshipped_current_page += 1; getShopUnshippedOrderOrder(shop_unshipped_current_page)"
                :disabled="shop_unshipped_current_page === 1" />
              <q-btn flat color="secondary" label="下一页" style="flex-grow: 1;"
                @click="shop_unshipped_current_page += 1; getShopUnshippedOrderOrder(shop_unshipped_current_page)"
                :disabled="shop_unshipped_current_page === shop_unshipped_total_page" />
            </div>
          </div>

          <div v-if="shop_refunding_order" style="margin-top: 3%;">
            <div v-if="shop_refunding_order_lst.length === 0">
              <div class="text-caption text-grey" style="text-align: center; margin-top: 10px;">暂时没有申请退款的订单哦</div>
            </div>
            <div v-else>
              <div v-for="order in shop_refunding_order_lst" :key="order">
                <q-card class="shop-card" flat bordered style="width: 95%; margin-top: 2%; display: flex;">
                  <q-card-section class="q-pt-xs" style="width: 100%; display: flex;">
                    <div class="customer-order-text" style="width: 100%; flex-grow: 5;">
                      <div class="text-overline text-grey-10">订单编号{{ order.userOrder.id }}</div>

                      <div class="q-mt-sm q-mb-xs" style="font-size: large;">商店名称{{
                        order.shop.shopName }}</div>
                      <div class="text-bold q-mt-sm q-mb-xs" style="font-size: large;">商品名称{{
                        order.goods.goodsName }}</div>
                      <div class="text-bold q-mt-sm q-mb-xs" style="margin-left: 3px; color: gray;">×
                        {{ order.userOrder.quantity }}</div>
                      <div class="text-bold q-mt-sm q-mb-xs">实付款：¥{{ order.userOrder.totalPrice }}</div>

                      <div class="text q-mt-sm q-mb-xs">收货人：{{ order.deliveryAddress.name }}&nbsp; &nbsp; 联系电话：{{
                        order.deliveryAddress.phoneNumber }}</div>
                      <div class="text q-mt-sm q-mb-xs">收货地址：{{ order.deliveryAddress.address }}</div>

                    </div>

                    <div class="customer-order-pict" style=" flex-grow: 5;">
                      <!-- TODO: 填上商品图片 -->
                      <q-img class="rounded-borders" :src="order.goods.image[0]"
                        style="width:200px; height:150px"></q-img>
                    </div>
                  </q-card-section>

                  <q-card-actions style="flex-grow: 0.5;">
                    <!-- TODO: 补充操作 -->
                    <q-btn flat style="color: #570089; font-size: large;" label="同意退款"
                      @click="shopAgreeRefunding(order)" />
                  </q-card-actions>
                </q-card>
              </div>
            </div>

            <div class="btn-box" style="display: flex;">
              <q-btn flat color="secondary" label="上一页" style="flex-grow: 1;"
                @click="shop_refunding_current_page += 1; getShopRefundingOrderOrder(shop_refunding_current_page)"
                :disabled="shop_refunding_current_page === 1" />
              <q-btn flat color="secondary" label="下一页" style="flex-grow: 1;"
                @click="shop_unshipped_current_page += 1; getShopRefundingOrderOrder(shop_refunding_current_page)"
                :disabled="shop_refunding_current_page === shop_refunding_total_page" />
            </div>
          </div>
        </div>

      </div>

    </q-page-container>

    <!-- 修改商品信息 -->
    <div class="editCommodity" v-if="editCommodity">
      <q-dialog v-model="editCommodity" width>
        <q-layout view="Lhh lpR fff" container class="bg-white">

          <q-header class="bg-primary">
            <q-toolbar>
              <q-btn flat @click="drawer = !drawer" round dense icon="menu" />
              <q-toolbar-title>修改商品信息</q-toolbar-title>
              <q-btn flat @click="drawerR = !drawerR" round dense icon="menu" />
              <q-btn flat v-close-popup round dense icon="close" />
            </q-toolbar>
          </q-header>

          <q-drawer bordered v-model="drawer" :width="200" :breakpoint="600" class="bg-grey-3 q-pa-sm">
            <div v-for="n in 50" :key="n">Drawer {{ n }} / 50</div>
          </q-drawer>

          <q-drawer side="right" bordered v-model="drawerR" :width="200" :breakpoint="300" class="bg-grey-3 q-pa-sm">
            <div v-for="n in 50" :key="n">Drawer {{ n }} / 50</div>
          </q-drawer>

          <q-page-container>
            <q-page padding>
              <q-form ref="myForm" action="http://120.46.154.28:9999/user" method="post" @submit="postProfileUpdate"
                @reset="onReset" class="q-gutter-md">
                <dl>
                  <dt>
                    <label class="label" for="userName">商品名称</label>
                  </dt>
                  <dd>
                    <q-input filled name="goodsName" v-model="editComName" @click="code = 0" :disable="viewOnly" />
                  </dd>
                </dl>

                <dl>
                  <dt>
                    <label class="label" for="userPhone">价格</label>
                  </dt>
                  <dd>
                    <q-input filled name="price" v-model="editComPrice" @click="code = 0" :disable="viewOnly" />
                  </dd>
                </dl>

                <dl>
                  <dt>商品描述</dt>
                  <dd>
                    <q-input filled name="description" v-model="editComDescription" :disable="viewOnly" />
                  </dd>
                </dl>

                <div class="button-box">
                  <div v-if="viewOnly" class="edit-button">
                    <q-btn color="secondary" label="修改信息" @click="viewEditModeChange" />
                  </div>

                  <div v-if="!viewOnly" class="submit-button">
                    <q-btn color="secondary" label="提交修改" @click="postCommodityEdit" />
                  </div>
                  <q-tooltip v-if="editNotification" anchor="bottom middle" self="top middle" :offset="[10, 10]">
                    <strong>{{ editMsg }}</strong>
                    (<q-icon name="keyboard_arrow_down" />)
                  </q-tooltip>
                </div>
              </q-form>
            </q-page>
          </q-page-container>
        </q-layout>
      </q-dialog>
    </div>

    <q-dialog v-model="show_activity_apply_fail">
      <q-card>
        <q-card-section class="q-pt-none text-h6" style="margin-top: 5%;">
          申请参加活动失败！
          <div style="font-weight: normal;">{{ activity_apply_response_message }}</div>
        </q-card-section>

        <q-card-actions align="right">
          <q-btn flat label="确认" color="secondary" @click="close_alert(show_activity_apply_fail)" v-close-popup />
        </q-card-actions>
      </q-card>
    </q-dialog>

    <q-dialog v-model="delete_commodity_confirm" no-click-outside-close>
      <q-layout view="Lhh lpR fff" container class="bg-white">

        <q-header class="bg-primary">
          <q-toolbar>
            <q-btn flat @click="drawer = !drawer" round dense icon="menu" />
            <q-toolbar-title>下架商品确认</q-toolbar-title>
            <q-btn flat @click="drawerR = !drawerR" round dense icon="menu" />
            <q-btn flat v-close-popup round dense icon="close" @click="delete_commodity_confirm = false" />
          </q-toolbar>
        </q-header>


        <q-page-container>
          <q-page padding>
            <q-card-section class="q-pt-none text-h6" style="margin-top: 5%;">
              确定要下架商品{{ delete_commodity.goodsName }}吗？
            </q-card-section>
            <q-form ref="myForm" @submit="deleteCommodity" @reset="onReset" class="q-gutter-md">
              <br>
              <q-input filled v-model="delete_commodity.goodsName" hint="商品名称" disable />

              <q-input filled v-model="delete_commodity.description" hint="商品描述" disable />

              <q-input filled v-model="delete_commodity.price" hint="商品价格" disable />

              <div class="q-pa-md">
                <q-carousel class="commodity_img" swipeable animated v-model="delete_commodity.slide" height="200px"
                  thumbnails infinite>
                  <q-carousel-slide v-for="(image, index) in delete_commodity.image" :key="index" :name="index + 1"
                    :img-src="image" />
                </q-carousel>
              </div>

              <div class="submit">
                <q-btn label="确认下架" type="submit" color="negative" flat class="q-ml-sm" style="font-size: large;"
                  @click="deleteCommodity(delete_commodity)" />
                <q-btn label="取消" type="reset" color="indigo-10" flat class="q-ml-sm" style="font-size: large;"
                  @click="delete_commodity_confirm = false" />
              </div>
            </q-form>
          </q-page>
        </q-page-container>

        <q-card-actions align="right">
          <q-btn flat label="取消" color="indigo-10" v-close-popup />
          <q-btn flat label="确认" color="pink-13" v-close-popup @click="deleteCommodity(delete_commodity)" />
        </q-card-actions>
      </q-layout>
    </q-dialog>

    <q-dialog v-model="operation_success_alert_layout">
      <q-card style="min-width: 350px">
        <q-card-section>
          <div>
            <div class="text-weight-bold">{{ operation_success_message }}</div>
          </div>
        </q-card-section>

        <q-card-actions align="right">
          <q-btn flat label="OK" color="secondary" v-close-popup
            @click="operation_success_alert_layout.value = false; " />
        </q-card-actions>
      </q-card>
    </q-dialog>

    <q-dialog v-model="operation_fail_alert_layout">
      <q-card style="min-width: 350px">
        <q-card-section>
          <div>
            <div class="text-weight-bold">{{ operation_fail_message }}</div>
          </div>
        </q-card-section>

        <q-card-actions align="right">
          <q-btn flat label="OK" color="secondary" v-close-popup @click="operation_fail_alert_layout.value = false; " />
        </q-card-actions>
      </q-card>
    </q-dialog>

  </q-layout>
</template>

<script setup>
import { ref, computed, getCurrentInstance } from 'vue'
import axios from 'axios'
import { onMounted } from 'vue'
const axiosInstance = axios.create({
  baseURL: 'http://120.46.154.28:9999',
});
let leftDrawerOpen = ref(false)

// 左侧边栏内项目icon-text列举
const links1 = [
  { icon: 'person', text: '个人账户' },
  { icon: 'star_border', text: '资金信息' }
]

const tab = ref('onShelve')
const layout = ref(false)
const layout_delete = ref(false)

let editCommodity = ref(false)
let viewOnly = ref(true)

const instance = getCurrentInstance()

// 用户操作成功or失败的提示
let operation_success_alert_layout = ref(false)
let operation_success_message = ref(null)
let operation_fail_alert_layout = ref(false)
let operation_fail_message = ref(null)

let show_commodity = ref(true)
let show_transition = ref(false)

// 商城活动
let all_activities = ref([])
let activity_apply_response_message = ref(null)
let activity_apply_response_code = ref(null)
let show_activity_apply_fail = ref(false)

// 店铺订单
let show_order = ref(false)

let shop_refunding_order_lst = ref([])  // 待退款
let shop_unshipped_order_lst = ref([])

let shop_refunding_total_page = ref(null)
let shop_unshipped_total_page = ref(null)

let shop_refunding_current_page = 1
let shop_unshipped_current_page = 1

let shop_refunding_order = ref(false)
let shop_unshipped_order = ref(false)

// 商店的售卖商品类型
let shop_category = ref([])

//上架商品
const goodsName = ref(null)
const description = ref(null)
const price = ref(null)
const image = ref([])
let new_commodity_category = ref(null)

//下架商品信息
let delete_commodity_confirm = ref(false)
let delete_commodity = ref(null)
const delete_goodsName = ref(null)
const delete_description = ref(null)
const delete_price = ref(null)

//修改申请记录
let editComId = ref(null)
let editComName = ref(null)
let editComDescription = ref(null)
let editComPrice = ref(null)
let editComShopid = ref(null)
let editComImage = ref(null)
let editComStatus = ref(null)
const shops = ref([])
const code = ref(null)
let transferExpanded = ref(false)
const shopId = localStorage.getItem('shopId')

// 商品展示（分类数组）
let onShelveCmdt = ref([])
let removedCmdt = ref([])
let applyingCmdt = ref([])

// 流水
let transitionColumn = [
  { name: 'transferName', label: '转账人', field: 'transferName' },
  { name: 'receiveName', label: '收款人', field: 'receiveName' },
  { name: 'amount', label: '金额（¥）', field: 'amount' },
]
let transitionRows = ref([])
let transaction_range = ref('all_transactions') // 流水的展示范围


const shopName = computed(() => {
  return shops.value ? shops.value.shopName : '';
});

const shopIntroduce = computed(() => {
  return shops.value ? shops.value.introduce : '';
});

const shopAddress = computed(() => {
  return shops.value ? shops.value.address : '';
});

const shopRegistrationTime = computed(() => {
  return shops.value ? shops.value.registrationTime : '';
});

const shopCategory = computed(() => {
  return shops.value ? shops.value.category : '';
});


const myForm = ref(null);

// 暂禁用
// 左侧边栏开闭
// function toggleLeftDrawer() {
// leftDrawerOpen.value = !leftDrawerOpen.value
// }

function update() {
  getShelvedCommodities()
  getApplication()
  getRemovedCommodities()
  getTransition()
  getAllSalesActivity()
  instance.proxy.$forceUpdate();
}

function checkprice(price) {
  if (isNaN(price)) {
    return false;
  }
  if (price < 0) {
    return false;
  }
  return true; // 输入值合法
}

function confirmDeleteCommodity(commodity) {
  delete_commodity.value = commodity
  delete_commodity_confirm.value = true
}

//下架商品
function deleteCommodity(commodity) {
  console.log('delete commodity parameter: ', commodity.id)
  axiosInstance.put('/Goods/remove',
    {
      id: commodity.id,
    },
    {
      params: {
        goodsId: commodity.id,
      }
    }).then((response) => {
      console.log('下架商品：', response)
      if (response.data['code'] === 20000) {
        operation_success_alert_layout.value = true
        operation_success_message.value = "商品已下架"
        delete_commodity_confirm.value = false
        update()
      } else {
        operation_fail_alert_layout.value = true
        operation_fail_message.value = '商品下架失败'
        delete_commodity_confirm.value = false
        udpate()
      }
    });
}


// 提交上架商品的表单
function onSubmit() {
  let new_com_category_lst = [new_commodity_category.value]
  console.log("商品上架类型", new_com_category_lst)
  axiosInstance.post('/Goods/addGoodsApply',
    {
      category: new_com_category_lst,
      goodsName: goodsName.value,
      description: description.value,
      price: price.value,
      shopId: shopId,
      image: image.value
    },
    {
      params: {
        shopId: shopId
      }
    }
  )
    .then((response) => {
      code.value = response.data['code'];
      if (code.value == 20000) {
        console.log('上架申请后layout:', layout.value);
        operation_success_alert_layout.value = true
        operation_success_message.value = "上架申请已提交"
        update()
        onReset()
        layout.value = false;
      } else {
        operation_fail_alert_layout.value = true
        operation_fail_message.value = '上架申请提交失败，', response.data['message']
        layout.value = false;
        update()
        onReset()
      }
      console.log("上架申请返回值", response.data);
    })
    .catch((error) => {
      console.error('Error:', error);
      // 处理错误
      myForm.value.resetValidation();
    });
}

function onReset() {
  goodsName.value = null
  description.value = null
  price.value = null
  image.value = []
  myForm.value.resetValidation();
}

function handleUpload(event) {
  console.log('handleUpload', event)
  const r = event.xhr.responseText;
  const r_obj = JSON.parse(r)
  console.log(r_obj)
  r_obj.message = 'http://120.46.154.28:9999/img/' + r_obj.message
  image.value.push(r_obj.message)
  console.log(image.value)
}


onMounted(() => {
  //显示该店铺的信息
  console.log('shopId: ', shopId)

  console.log('current shopID: ', shopId)
  axiosInstance.get('/shop/showShopByShopId', {
    params: {
      shopId: shopId
    }
  }).then((response) => {
    const r = response.data['data']
    const [year, month, day] = r.registrationTime;
    r.registrationTime = `${year}-${month}-${day}`;
    shops.value = r
    console.log('shop message: ', shops.value)
  });

  getShelvedCommodities()
  getApplication()
  getRemovedCommodities()
  getTransition()
  getAllSalesActivity()
});

// 获取当前所有商城活动
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
        all_activities.value.push(item)
      }
    });
  });
}


function toEditCommodity(commodity) {
  console.log('edit commodity msg: ', commodity);
  editCommodity.value = true
  editComName.value = commodity.goodsName
  editComId.value = commodity.id
  editComDescription.value = commodity.description
  editComPrice.value = commodity.price
  editComShopid.value = commodity.shopId
  editComImage.value = commodity.image
  editComStatus.value = commodity.status
}

function viewEditModeChange() {
  viewOnly.value = !viewOnly.value
  console.log('view mode: ', viewOnly.value)
}

function applyAddCommodity() {
  layout.value = true
  goodsName.value = null
  description.value = null
  price.value = null
  image.value = []
}

function postCommodityEdit() {
  console.log('post edit commodity: \ngoods id: ', editComId.value,
    '\ngoods name: ', editComName.value,
    '\ngoods price: ', editComPrice.value,
    '\ngoods description: ', editComDescription.value,
    '\ngoods shop id: ', editComShopid.value,
    '\ngoods image: ', editComImage.value,)

  axiosInstance
    .post('/Goods/updateGoodsApply',
      {
        goodsName: editComName.value,
        description: editComDescription.value,
        price: editComPrice.value,
        shopId: editComShopid.value,
        image: editComImage.value
      },
      {
        params: {
          goodsId: editComId.value,
        }
      }
    )
    .then((response) => {
      //console.log('Response data:', response.data)
      // 处理返回数据
      // 导航到另一个页面
      console.log('shangjia')
      code.value = response.data['code'];
      if (code.value == 20000) {
        layout.value = false;
        console.log('layout');
        console.log(layout.value);
      }
      console.log(response.data);
      viewOnly.value = true
      editCommodity.value = false
      update()
    })
    .catch((error) => {
      console.error('Error:', error);
      // 处理错误
      // myForm.value.resetValidation();
    });
}

// 获取改店铺的所有已上架商品
function getShelvedCommodities() {
  onShelveCmdt = ref([])
  axiosInstance.get('/Goods/showAddRecord_1', {
    params: {
      shopId: shopId
    }
  }).then((response) => {
    const r = response.data['data']
    r.forEach(function (item) {
      // item.id is shopId!
      item.status = '在售中'
    });
    onShelveCmdt.value = r
    console.log('获取在售商品(record 1)：', onShelveCmdt.value)
  });
  // 修改商品信息成功的案例也算做上架中的商品
  axiosInstance.get('/Goods/showUpdateRecord_8', {
    params: {
      shopId: shopId
    }
  }).then((response) => {
    const r = response.data['data']
    r.forEach(function (item) {
      // console.log(item.id)
      // item.id is shopId!
      if (item !== null) {
        item.status = '在售中（修改信息成功）'
        onShelveCmdt.value.push(item)
      }
    });
  });
  // 修改信息失败也是上架商品
  axiosInstance.get('/Goods/showUpdateRecord_9', {
    params: {
      shopId: shopId
    }
  }).then((response) => {
    const r = response.data['data']
    r.forEach(function (item) {
      // console.log(item.id)
      // item.id is shopId!
      if (item !== null) {
        item.status = '在售中（修改信息失败）'
        onShelveCmdt.value.push(item)
      }
    });
  });
}

// 获取改店铺的所有申请中 & 申请失败的商品
function getApplication() {
  applyingCmdt = ref([])
  // 上架等待审批
  axiosInstance.get('/Goods/showAddRecord_0', {
    params: {
      shopId: shopId
    }
  }).then((response) => {
    const r = response.data['data']
    r.forEach(function (item) {
      // console.log(item.id)
      // item.id is shopId!
      if (item !== null) {
        item.status = '上架申请审批中'
        applyingCmdt.value.push(item)
      }
    });
    // console.log('获取申请商品(record 0)：', applyingCmdt.value)
  });
  // 上架失败
  axiosInstance.get('/Goods/showAddRecord_2', {
    params: {
      shopId: shopId
    }
  }).then((response) => {
    const r = response.data['data']
    r.forEach(function (item) {
      // console.log(item.id)
      // item.id is shopId!
      if (item !== null) {
        item.status = '上架申请失败'
        applyingCmdt.value.push(item)
      }
    });
    // if (r !== []){
    //     applyingCmdt.value.push(r)
    //   }
    // console.log('获取申请商品(record 2)：', applyingCmdt.value)
  });
  // 修改信息审批中
  axiosInstance.get('/Goods/showUpdateRecord_7', {
    params: {
      shopId: shopId
    }
  }).then((response) => {
    const r = response.data['data']
    r.forEach(function (item) {
      // console.log(item.id)
      // item.id is shopId!
      if (item !== null) {
        item.status = '修改信息申请审批中'
        applyingCmdt.value.push(item)
      }
    });
    // if (r !== []){
    //     applyingCmdt.value.push(r)
    //   }
    // console.log('获取申请商品(record 7)：', applyingCmdt.value)
  });
  // 修改信息失败
  axiosInstance.get('/Goods/showUpdateRecord_9', {
    params: {
      shopId: shopId
    }
  }).then((response) => {
    const r = response.data['data']
    r.forEach(function (item) {
      // console.log(item.id)
      // item.id is shopId!
      if (item !== null) {
        item.status = '修改信息申请失败'
        applyingCmdt.value.push(item)
      }
    });
    // if (r !== []){
    //     applyingCmdt.value.push(r)
    //   }
    // console.log('获取申请商品(record 9)：', applyingCmdt.value)
  });
}

// 获取所有已下架商品
function getRemovedCommodities() {
  removedCmdt = ref([])
  axiosInstance.get('/Goods/showDeleteRecord_5', {
    params: {
      shopId: shopId
    }
  }).then((response) => {
    const r = response.data['data']
    // console.log('showDeleteRecord_5')
    r.forEach(function (item) {
      // console.log(item.id)
      // item.id is shopId!
      if (item !== null) {
        item.status = '已下架'
        removedCmdt.value.push(item)
      }
    });
    console.log('获取下架商品(record 5)：', removedCmdt.value)
  });
}

// 获取店铺流水
function getTransition() {
  transitionRows.value = []
  if (transaction_range.value === 'all_transactions') {
    console.log('current shopid: ', shopId)
    axiosInstance.post('/transferRecords/shop', {
      id: shopId
    }, {
      params: {
        id: shopId
      }
    }).then((response) => {
      const r = response.data[1]['data']
      console.log('get shop transfer records: ', r);
      r.forEach(function (item) {
        const [year, month, day] = item.time;
        item.time = `${year}-${month}-${day}`;
        transitionRows.value.push(item)
      });
    });
    console.log('transition: ', transitionRows.value)
  } else if (transaction_range.value === 'last_month_transactions') {
    console.log('current shopid: ', shopId)
    axiosInstance.post('/transferRecords/shopWithinOneMonth', {
      id: shopId
    }, {
      params: {
        id: shopId
      }
    }).then((response) => {
      const r = response.data[1]['data']
      console.log('get shop transfer records: ', r);
      r.forEach(function (item) {
        const [year, month, day] = item.time;
        item.time = `${year}-${month}-${day}`;
        transitionRows.value.push(item)
      });
    });
    console.log('transition: ', transitionRows.value)
  } else if (transaction_range.value === 'last_week_transactions') {
    console.log('current shopid: ', shopId)
    axiosInstance.post('/transferRecords/shopWithinOneWeek', {
      id: shopId
    }, {
      params: {
        id: shopId
      }
    }).then((response) => {
      const r = response.data[1]['data']
      console.log('get shop transfer records: ', r);
      r.forEach(function (item) {
        const [year, month, day] = item.time;
        item.time = `${year}-${month}-${day}`;
        console.log(item);
        transitionRows.value.push(item)
      });
    });
    console.log('transition: ', transitionRows.value)
  }
}

function applyForActivity(activity) {
  console.log('申请参加活动 活动id: ', activity.id, "\tshop id: ", shopId)
  axiosInstance.put('/event/applyEvent',
    {
      shopId: shopId,
      eventId: activity.id,
    },
    {
      params: {
        shopId: shopId,
        eventId: activity.id,
      }
    }).then((response) => {
      console.log('申请参加活动返回信息', response)
      activity_apply_response_message.value = response['data']['message']
      activity_apply_response_code.value = response['data']['code']
      if (activity_apply_response_code.value === 20000) {
        operation_success_alert_layout.value = true
        operation_success_message.value = '申请参加活动成功'
      } else {
        operation_fail_alert_layout.value = true
        operation_fail_message.value = activity_apply_response_message.value
      }
      update()
    });
}

function close_alert(alert_flag) {
  alert_flag = !alert_flag
}


function showShopRefundingOrder() {
  shop_refunding_order.value = true
  shop_unshipped_order.value = false
  shop_refunding_current_page = 1
  getShopRefundingOrderOrder(shop_refunding_current_page)
}

function showShopUnshippedOrder() {
  shop_refunding_order.value = false
  shop_unshipped_order.value = true
  shop_unshipped_current_page = 1
  getShopUnshippedOrderOrder(shop_unshipped_current_page)
}

// TODO：日期
function getShopRefundingOrderOrder(shop_refunding_current_page) {
  shop_refunding_order_lst.value = [];
  const pageSize = 5;

  console.log("Show shop id when getting shop refunding order: ", shopId, "\npage no: ", shop_refunding_current_page);
  axiosInstance.get('/userOrder/showShopRefundingOrder', {
    params: {
      shopId: shopId,
      pageNo: shop_refunding_current_page,
      pageSize: pageSize,
    }
  }).then((response) => {
    const shop_refunding_orders = response.data.data.userOrderPlusList; // 获取订单数据
    console.log("shop refunding order 请求数据: ", response)
    shop_refunding_orders.forEach(function (item) {
      console.log("[shop refunding orders] item: ", item)
      // if (item !== null) {
      //   const [year, month, day] = item.userOrder.date;
      //   item.userOrder.date = `${year}-${month}-${day}`;
      shop_refunding_order_lst.value.push(item);
      // }
    });
    shop_refunding_total_page.value = response.data.data.pages; // 总页数
    console.log("[shop refunding order] set total page: ", shop_refunding_total_page.value)
  });
  console.log("shop refunding orders: ", shop_refunding_order_lst.value)
}

// TODO：显示栈满？日期问题
function getShopUnshippedOrderOrder(shop_unshipped_current_page) {
  shop_unshipped_order_lst.value = [];
  const pageSize = 5;

  console.log("Show shop id when getting shop refunding order: ", shopId, "\npage no: ", shop_unshipped_current_page);
  axiosInstance.get('/userOrder/showShopUnshippedOrder', {
    params: {
      shopId: shopId,
      pageNo: shop_unshipped_current_page,
      pageSize: pageSize,
    }
  }).then((response) => {
    const shop_unshipped_orders = response.data.data.userOrderPlusList; // 获取订单数据
    console.log("shop refunding order 请求数据: ", response)
    shop_unshipped_orders.forEach(function (item) {
      // console.log("[unpaid orders] item: ", item)
      // if (item !== null) {
      // const [year, month, day] = item.userOrder.date;
      // item.userOrder.date = `${year}-${month}-${day}`;
      shop_unshipped_order_lst.value.push(item);
      // }
    });
    shop_unshipped_total_page.value = response.data.data.pages; // 总页数
    console.log("[shop unshipped order] set total page: ", shop_unshipped_total_page.value)
  });
  console.log("shop unshipped orders: ", shop_unshipped_total_page.value)
}

function shopAgreeRefunding(order) {
  current_time.value = getFormattedDate()
  console.log('同意退款: ', order, "\ncurrent time: ", current_time.value)
  axiosInstance.put('/userOrder/refunded',
    {
      addressId: order.deliveryAddress.id,
      date: order.userOrder.date,
      deleted: order.userOrder.deleted,
      eventId: order.userOrder.eventId,
      goodsId: order.goods.goodsId,
      id: order.userOrder.id,
      quantity: order.userOrder.quantity,
      shopId: order.shop.shopId,
      status: order.userOrder.status,
      totalPrice: order.userOrder.totalPrice,
      unitPrice: order.userOrder.unitPrice,
      userId: order.userOrder.userId
    },
    {
      params: {
        stime: current_time.value
      }
    }).then((response) => {
      console.log("确认退货退款返回数据：", response)
      if (response.data['code'] === 20000) {
        operation_success_alert_layout.value = true
        operation_success_message.value = '已同意退货退款'
      } else {
        operation_fail_message.value = response.data['message']
        operation_fail_alert_layout.value = true
      }
    });
}

function shopShipOrder(orderId) {
  current_time.value = getFormattedDate()
  console.log('发货: ', orderId)
  axiosInstance.put('/merchantOrder/ship',
    {
      orderId: orderId,
    },
    {
      params: {
        orderId: orderId,
      }
    }).then((response) => {
      console.log("发货返回数据：", response)
      if (response.data['code'] === 20000) {
        operation_success_alert_layout.value = true
        operation_success_message.value = '已发货'
      } else {
        operation_fail_message.value = response.data['message']
        operation_fail_alert_layout.value = true
      }
    });
}

let current_time = ref(null)
// 获取当前日期 YYYY-MM-DD
function getFormattedDate() {
  const now = new Date();
  const isoDate = now.toISOString();
  return isoDate.substr(0, 10);
}

</script>

<style lang="sass" scoped>
.my-card
  width: 100%
  max-width: 350px
  height: 100%
  max-height: 430px
</style>
