<template>
  <q-layout view="hHh lpR fFf">

    <q-header elevated class="bg-primary text-white">
      <q-toolbar>
        <q-btn dense flat round icon="menu" @click="toggleLeftDrawer" />

        <q-toolbar-title class="text-italic">
          <q-avatar>
            <img src="https://cdn.quasar.dev/logo-v2/svg/logo-mono-white.svg">
          </q-avatar>
          首页
        </q-toolbar-title>
        <q-btn flat round dense icon="update" class="q-mr-xs" @click="update"></q-btn>

        <q-btn-dropdown class="glossy" color="dark" label="账户设置">
          <div class="row no-wrap q-pa-md">
            <div class="column items-center">
              <q-avatar size="72px">
                <img src="https://cdn.quasar.dev/img/boy-avatar.png">
              </q-avatar>

              <div class="text-subtitle1 q-mt-md q-mb-xs">{{ username }}</div>

              <q-btn to="/login" color="primary" label="Logout" push size="sm" v-close-popup />
            </div>
          </div>
        </q-btn-dropdown>
      </q-toolbar>
    </q-header>

    <q-drawer v-model="leftDrawerOpen" show-if-above bordered>
      <q-list>
        <q-item-label header>Essential Links</q-item-label>
        <q-item clickable target="_blank" rel="noopener" @click="toggle">
          <q-item-section avatar>
            <q-icon name="face" />
          </q-item-section>
          <q-item-section> Dark theme </q-item-section>
        </q-item>
        <router-link to="/admin" class="router-link-class">
          <q-item clickable target="_blank" rel="noopener" @click="showAdminMainPage">
            <q-item-section avatar>
              <q-icon name="perm_identity" />
            </q-item-section>
            <q-item-section> Admin </q-item-section>
          </q-item>
        </router-link>

        <q-item clickable target="_blank" rel="noopener" @click="showTransitionMode">
          <q-item-section avatar>
            <q-icon name="open_in_new" />
          </q-item-section>
          <q-item-section> 中间账户 </q-item-section>
        </q-item>

        <q-item clickable target="_blank" rel="noopener" @click="showProfitMode">
          <q-item-section avatar>
            <q-icon name="open_in_new" />
          </q-item-section>
          <q-item-section> 利润账户 </q-item-section>
        </q-item>

        <q-item clickable target="_blank" rel="noopener" @click="showAllSalesActivity">
          <q-item-section avatar>
            <q-icon name="map" />
          </q-item-section>
          <q-item-section> 商城活动 </q-item-section>
        </q-item>

        <q-item>
          <q-item-section>
            <img src="../../assets/log.png" alt="" />
          </q-item-section>
        </q-item>
      </q-list>
    </q-drawer>

    <q-page-container>
      <div class="q-pa-md" v-if="showAllApplication">

        <div class="applicationTypeSelect" style="width: 100%">
          <q-btn flat rounded color="secondary" label="待查看" @click="selectRemainChecked"
            style="margin-left: 20%; font-size: 105%; font-weight: normal" />
          <q-btn flat rounded color="secondary" label="已同意" @click="selectApproved"
            style="margin-left: 20%;font-size: 105%; font-weight: normal;" />
          <q-btn flat rounded color="secondary" label="已拒绝" @click="selectRejected"
            style="margin-left: 20%; font-size: 105%; font-weight: normal" />
        </div>

        <q-separator style="width: 100%; margin-top: 1%;" />

        <p v-if="!showRejectedApps && !showApprovedApps && !showRemainCheckedApps"
          style="font-size: large; margin-top: 10%; margin-left: 40%;">
          请选择您想要查看的申请类型
        </p>

        <div class="remainCheckedApps" v-if="showRemainCheckedApps">
          <p style="font-size: large; margin-top: 2%; margin-left: 2%;">待批准的店铺申请</p>
          <q-table :rows="rcShopAppRows" :columns="rcShopAppColumns" row-key="id" :pagination="true"
            style="margin-top: 2%; margin-left: 1%;">
            <template v-slot:body-cell-action="{ row }">
              <q-btn color="positive" class="q-mx-auto" icon="check" @click="approveApplication(row)" />
              <q-btn color="negative" class="q-mx-auto" icon="clear" @click="disapproveApplication(row)" />
            </template>
          </q-table>

          <q-separator style="width: 100%; margin-top: 3%;" />

          <p style="font-size: large; margin-top: 2%; margin-left: 2%;">待批准的商品申请</p>
          <div class="flex q-justify-around" style="width:800px">
            <!-- 待审批的上架申请 -->

            <div v-for="commodity in rcCommodityUpApp" :key="commodity" class="flex q-py-xs" style="width: 400px;">
              <q-card class="edit-commodity-card" style="width: 350px;">
                <div class="q-pa-md">
                  <q-carousel class="commodity_img" swipeable animated v-model="commodity.slide" height="200px" thumbnails
                    infinite>
                    <q-carousel-slide v-for="(image, index) in commodity.image" :key="index" :name="index + 1"
                      :img-src="image" />
                  </q-carousel>
                </div>


                <q-card-section>
                  <div class="row no-wrap items-center">
                    <div class="col text-h6 ellipsis">
                      {{ commodity.goodsName }}
                    </div>
                  </div>
                  <div class="text-subtitle1">
                    ¥ price: {{ commodity.price }}
                  </div>
                  <div class="text-caption text-grey">
                    {{ commodity.description }}
                  </div>
                </q-card-section>

                <q-card-actions>
                  <q-btn color="positive" class="q-mx-auto" icon="check" @click="approveCommodityUpdate(commodity)" />
                  <q-btn color="negative" class="q-mx-auto" icon="clear" @click="disapproveCommodityUpdate(commodity)" />
                </q-card-actions>
              </q-card>
              <q-separator />
            </div>

            <!-- 待审批的修改信息申请 -->
            <div v-for="commodity in rcCommodityEditApp" :key="commodity" class="flex q-py-xs justify-around"
              style="width: 400px">
              <q-card class="edit-commodity-card" style="width: 350px;">
                <div class="q-pa-md">
                  <q-carousel class="commodity_img" swipeable animated v-model="commodity.slide" height="200px" thumbnails
                    infinite>
                    <q-carousel-slide v-for="(image, index) in commodity.image" :key="index" :name="index + 1"
                      :img-src="image" />
                  </q-carousel>
                </div>

                <q-card-section>
                  <div class="row no-wrap items-center">
                    <div class="col text-h6 ellipsis">
                      {{ commodity.goodsName }}
                    </div>
                  </div>
                  <div class="text-subtitle1">
                    ¥ price: {{ commodity.price }}
                  </div>
                  <div class="text-caption text-grey">
                    {{ commodity.description }}
                  </div>
                </q-card-section>

                <q-card-actions>
                  <q-btn color="positive" class="q-mx-auto" icon="check" @click="approveCommodityEdit(commodity)" />
                  <q-btn color="negative" class="q-mx-auto" icon="clear" @click="disapproveCommodityEdit(commodity)" />
                </q-card-actions>
              </q-card>
            </div>
          </div>

        </div>

        <div class="approvedApps" v-if="showApprovedApps">
          <p style="font-size: large; margin-top: 2%; margin-left: 2%;">已批准的店铺申请</p>
          <q-table :rows="aShopAppRows" :columns="aShopAppColumns" row-key="shopName" :pagination="true"
            style="margin-top: 2%; margin-left: 1%;" />
          <q-separator style="width: 100%; margin-top: 3%;" />

          <p style="font-size: large; margin-top: 2%; margin-left: 2%;">已同意的商品申请</p>
          <div class="flex q-justify-around" style="width:800px">
            <!-- 已通过的上架申请 -->

            <div v-for="commodity in aCommodityUpApp" :key="commodity" class="flex q-py-xs justify-around"
              style="width: 400px;">
              <q-card class="edit-commodity-card" style="width: 350px;">
                <div class="q-pa-md">
                  <q-carousel class="commodity_img" swipeable animated v-model="commodity.slide" height="200px" thumbnails
                    infinite>
                    <q-carousel-slide v-for="(image, index) in commodity.image" :key="index" :name="index + 1"
                      :img-src="image" />
                  </q-carousel>
                </div>

                <q-card-section>
                  <div class="row no-wrap items-center">
                    <div class="col text-h6 ellipsis">
                      {{ commodity.goodsName }}
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

            <!-- 已通过的修改信息申请 -->
            <div v-for="commodity in aCommodityEditApp" :key="commodity" class="flex q-py-xs justify-around"
              style="width: 400px">
              <q-card class="edit-commodity-card" style="width: 350%;">
                <div class="q-pa-md">
                  <q-carousel class="commodity_img" swipeable animated v-model="commodity.slide" height="200px" thumbnails
                    infinite>
                    <q-carousel-slide v-for="(image, index) in commodity.image" :key="index" :name="index + 1"
                      :img-src="image" />
                  </q-carousel>
                </div>

                <q-card-section>
                  <div class="row no-wrap items-center">
                    <div class="col text-h6 ellipsis">
                      {{ commodity.goodsName }}
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
            </div>
          </div>

        </div>

        <div class="rejectedApps" v-if="showRejectedApps">
          <p style="font-size: large; margin-top: 2%; margin-left: 2%;">已拒绝的店铺申请</p>
          <q-table v-if="showRejectedApps" :rows="rShopAppRows" :columns="rShopAppColumns" row-key="name"
            style="margin-top: 2%; margin-left: 1%;" />

          <q-separator style="width: 100%; margin-top: 3%;" />

          <p style="font-size: large; margin-top: 2%; margin-left: 2%;">已拒绝的商品申请</p>
          <div class="flex q-justify-around" style="width:800px">
            <!-- 已拒绝的上架申请 -->
            <div v-for="commodity in rCommodityUpApp" :key="commodity" class="flex q-py-xs justify-around"
              style="width: 400px">
              <q-card class="edit-commodity-card" style="width: 350px;">
                <div class="q-pa-md">
                  <q-carousel class="commodity_img" swipeable animated v-model="commodity.slide" height="200px" thumbnails
                    infinite>
                    <q-carousel-slide v-for="(image, index) in commodity.image" :key="index" :name="index + 1"
                      :img-src="image" />
                  </q-carousel>
                </div>

                <q-card-section>
                  <div class="row no-wrap items-center">
                    <div class="col text-h6 ellipsis">
                      {{ commodity.goodsName }}
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
            <!-- 已拒绝的修改信息申请 -->
            <div v-for="commodity in rCommodityEditApp" :key="commodity" class="flex q-py-xs justify-around"
              style="width: 400px">
              <q-card class="edit-commodity-card" style="width: 350px">
                <div class="q-pa-md">
                  <q-carousel class="commodity_img" swipeable animated v-model="commodity.slide" height="200px" thumbnails
                    infinite>
                    <q-carousel-slide v-for="(image, index) in commodity.image" :key="index" :name="index + 1"
                      :img-src="image" />
                  </q-carousel>
                </div>

                <q-card-section>
                  <div class="row no-wrap items-center">
                    <div class="col text-h6 ellipsis">
                      {{ commodity.goodsName }}
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
            </div>
          </div>

        </div>
      </div>

      <!-- 商城中间账户展示 -->
      <div class="transitionAccount" v-if="showTransition && !showAllApplication && !showProfit && !showSalesActivity">
        <q-card class="my-card" flat bordered style="max-width: 100%; width: 70%; margin-left: 5%; margin-top: 3%;">

          <q-card-section>
            <div class="text-overline text-orange-9">商城中间账户</div>
            <div class="text-h5 q-mt-sm q-mb-xs">余额：¥{{ intermediateAccount }}</div>
          </q-card-section>

          <q-card-actions>
            <q-btn flat color="secondary" label="点击右侧下拉查看转账记录" style="margin-left: 0.5%;" />

            <q-space />

            <div class="option-box">
              <q-btn color="grey" round flat dense
                :icon="intermediateExpanded ? 'keyboard_arrow_up' : 'keyboard_arrow_down'"
                @click="intermediateExpanded = !intermediateExpanded" style="float: right;" />
            </div>
          </q-card-actions>

          <!-- 商城中间账户的流水 -->
          <q-slide-transition>
            <div v-show="intermediateExpanded">
              <q-separator style="margin-top: 1%;" />

              <div class="transaction-option-box" style="margin-left: 1%; margin-top: 1%;">
                <q-radio v-model="transaction_range" checked-icon="task_alt" unchecked-icon="panorama_fish_eye"
                  val="all_transactions" label="所有记录" @update:model-value="getTransfer" />
                <q-radio v-model="transaction_range" checked-icon="task_alt" unchecked-icon="panorama_fish_eye"
                  val="last_month_transactions" label="进一个月的记录" @update:model-value="getTransfer" />
                <q-radio v-model="transaction_range" checked-icon="task_alt" unchecked-icon="panorama_fish_eye"
                  val="last_week_transactions" label="近一周的记录" @update:model-value="getTransfer" />
              </div>

              <q-separator style="margin-top: 1%; width: 95%; margin-left: 2%;" />
              <q-table :rows="intermediateAccountTrans" :columns="TransitionColumn" row-key="transferName"
                style="margin-top: 2%; margin-left: 1%;" />
            </div>
          </q-slide-transition>
        </q-card>
      </div>

      <!-- 商城利润账户展示 -->
      <div class="profitAccount" v-if="showProfit && !showTransition && !showAllApplication && !showSalesActivity">
        <q-card class="my-card" flat bordered style="max-width: 100%; width: 70%; margin-left: 5%; margin-top: 3%;">

          <q-card-section>
            <div class="text-overline text-orange-9">商城利润账户</div>
            <div class="text-h5 q-mt-sm q-mb-xs">目前利润：¥{{ profitAccount }}</div>
          </q-card-section>

          <q-card-actions>
            <q-btn flat color="secondary" label="点击右侧下拉查看流水" style="margin-left: 0.5%;" />

            <q-space />

            <q-btn color="grey" round flat dense :icon="profitExpanded ? 'keyboard_arrow_up' : 'keyboard_arrow_down'"
              @click="profitExpanded = !profitExpanded" />
          </q-card-actions>

          <q-slide-transition>
            <div v-show="profitExpanded">
              <q-separator style="margin-top: 1%;" />

              <div class="transaction-option-box" style="margin-left: 1%; margin-top: 1%;">
                <q-radio v-model="transaction_range" checked-icon="task_alt" unchecked-icon="panorama_fish_eye"
                  val="all_transactions" label="所有记录" @update:model-value="getTransfer" />
                <q-radio v-model="transaction_range" checked-icon="task_alt" unchecked-icon="panorama_fish_eye"
                  val="last_month_transactions" label="近一个月的记录" @update:model-value="getTransfer" />
                <q-radio v-model="transaction_range" checked-icon="task_alt" unchecked-icon="panorama_fish_eye"
                  val="last_week_transactions" label="近一周的记录" @update:model-value="getTransfer" />
              </div>

              <q-separator style="margin-top: 1%; width: 95%; margin-left: 2%;" />
              <q-table :rows="profitAccountTrans" :columns="TransitionColumn" row-key="transferName"
                style="margin-top: 2%; margin-left: 1%;" />
            </div>
          </q-slide-transition>
        </q-card>
      </div>

      <!-- 商城活动展示 -->
      <div class="salesActivity" v-if="showSalesActivity && !showProfit && !showTransition && !showAllApplication"
        style="width: 100%;">
        <div class="btn-col" style="display: flex; margin-top: 3%;">
          <q-btn flat color="secondary" label="所有活动" style="flex-grow: 1;" @click="showActivitysList" />
          <q-btn flat color="secondary" label="活动申请" style="flex-grow: 1;" @click="showActivityApplicationList" />
        </div>

        <q-separator style="width: 98%; margin-left: 1%; " />

        <div v-if="show_activity_list" class="all-activities" style="margin-left: 2%; width: 95%; margin-top: 2%;">
          <q-btn outline rounded color="deep-purple-6" label="新活动" style="margin-top: 2%;"
            @click="add_sales_activity_layout = true" />
          <q-card class="my-card" style="margin-top: 2%;">
            <q-card-section>
              <div class="text-h5 q-mt-sm q-mb-xs">商城活动</div>
              <div v-if="all_activities.length === 0" class="no-activity">
                <div class="text-caption text-grey" style="text-align: center; margin-top: 10px;">暂时没有活动</div>
              </div>
              <div v-else>
                <div v-for="activity in all_activities" :key="activity">
                  <q-card class="my-card" flat bordered style="margin-top: 2%;">

                    <q-card-section>
                      <div class="text-overline text-orange-9">{{ activity.startDate }}至{{ activity.endDate }}</div>
                      <div class="text-h5 q-mt-sm q-mb-xs">满{{ activity.amount }}减{{ activity.discount }}</div>
                      可参与活动的商品：
                      <div class="activity-category" v-for="category in activity.category" :key="category"
                        style="display: inline-block;">
                        {{ category }}&nbsp;
                      </div>
                      <div class="shop-fund">参加活动的店铺注册资金下限：¥{{ activity.shopFund }}</div>
                      <div class="shop-selling-amount">参加活动的店铺月销量下限：¥{{ activity.shopSales }}</div>
                      <div class="shop-monthly-profit">参加活动的店铺月销售额下限：{{ activity.shopProfit }}件</div>

                    </q-card-section>

                  </q-card>
                </div>
              </div>
            </q-card-section>
          </q-card>
        </div>

        <div v-if="show_activity_application_list" class="activity-applies"
          style="margin-left: 2%; width: 95%; margin-top: 2%;">
          <p style="font-size: large; margin-top: 2%; margin-left: 2%;">待批准的活动申请</p>
          <div class="rcSaleActivityApps" style="width: 98%; margin-left: 1%;">
            <div v-for="app in rc_activity_app" :key="app">
              <q-card class="my-card" flat bordered style="margin-top: 2%;">
                <q-card-section horizontal>
                  <q-card-section class="q-pt-xs">
                    <div class="RCactivityApp-info-box">
                      <div class="text-overline text-orange-9">{{ app.event.startDate }}至{{
                        app.event.endDate }}</div>
                      <div style="font-weight: bold;">申请人：{{ app.shop.shopName }}</div>
                      申请人售卖的商品类别：
                      <div class="apply-shop-category" v-for="category in app.shop.category" :key="category"
                        style="display: inline-block;">
                        {{ category }}&nbsp;
                      </div>
                      <div class="q-mt-sm q-mb-xs" style="font-weight: bold;">申请活动：满{{ app.event.amount }}减{{
                        app.event.discount }}</div>
                      可参与活动的商品：
                      <div class="activity-category" v-for="category in app.event.category" :key="category"
                        style="display: inline-block; ">
                        {{ category }}&nbsp;
                      </div>
                    </div>
                  </q-card-section>
                </q-card-section>

                <q-separator style="width: 98%; margin-left: 1%;" />

                <q-card-actions>
                  <q-btn flat @click="approveSalesActivityApp(activity_app)">
                    批准
                  </q-btn>
                  <q-btn flat color="primary" @click="disapproveSalesActivityApp">
                    拒绝
                  </q-btn>
                </q-card-actions>
              </q-card>
            </div>
          </div>

          <q-separator style="width: 98%; margin-left: 1%;  margin-top: 3%;" />

          <p style="font-size: large; margin-top: 2%; margin-left: 2%;">已批准的活动申请</p>
          <div class="rcSaleActivityApps" style="width: 98%; margin-left: 1%;" v-for="activity_app in a_activity_app"
            :key="activity_app">
            <q-card class="my-card" flat bordered style="margin-top: 2%;">
              <q-card-section horizontal>
                <q-card-section class="q-pt-xs">
                  <div class="AactivityApp-info-box">
                    <div class="text-overline text-orange-9">{{ activity_app.event.startDate }}至{{
                      activity_app.event.endDate }}</div>
                    <div style="font-weight: bold;">申请人：{{ activity_app.shop.shopName }}</div>
                    申请人售卖的商品类别：
                    <div class="apply-shop-category" v-for="category in activity_app.shop.category" :key="category"
                      style="display: inline-block;">
                      {{ category }}&nbsp;
                    </div>
                    <div class="q-mt-sm q-mb-xs" style="font-weight: bold;">申请活动：满{{ activity_app.event.amount }}减{{
                      activity_app.event.discount }}</div>
                    可参与活动的商品：
                    <div class="activity-category" v-for="category in activity_app.event.category" :key="category"
                      style="display: inline-block; ">
                      {{ category }}&nbsp;
                    </div>
                  </div>
                </q-card-section>
              </q-card-section>
            </q-card>
          </div>

          <q-separator style="width: 98%; margin-left: 1%; margin-top: 3%;" />

          <p style="font-size: large; margin-top: 2%; margin-left: 2%;">已拒绝的活动申请</p>
          <div class="rcSaleActivityApps" style="width: 98%; margin-left: 1%;" v-for="activity_app in r_activity_app"
            :key="activity_app">
            <q-card class="my-card" flat bordered style="margin-top: 2%;">
              <q-card-section horizontal>
                <q-card-section class="q-pt-xs">
                  <div class="RactivityApp-info-box">
                    <div class="text-overline text-orange-9">{{ activity_app.event.startDate }}至{{
                      activity_app.event.endDate }}</div>
                    <div style="font-weight: bold;">申请人：{{ activity_app.shop.shopName }}</div>
                    申请人售卖的商品类别：
                    <div class="apply-shop-category" v-for="category in activity_app.shop.category" :key="category"
                      style="display: inline-block;">
                      {{ category }}&nbsp;
                    </div>
                    <div class="q-mt-sm q-mb-xs" style="font-weight: bold;">申请活动：满{{ activity_app.event.amount }}减{{
                      activity_app.event.discount }}</div>
                    可参与活动的商品：
                    <div class="activity-category" v-for="category in activity_app.event.category" :key="category"
                      style="display: inline-block; ">
                      {{ category }}&nbsp;
                    </div>
                  </div>
                </q-card-section>
              </q-card-section>
            </q-card>
          </div>
        </div>

      </div>


      <q-dialog v-model="add_sales_activity_layout" no-click-outside-close>
        <q-layout view="Lhh lpR fff" container class="bg-white">
          <q-header class="bg-primary">
            <q-toolbar>
              <q-btn flat @click="drawer = !drawer" round dense icon="menu" />
              <q-toolbar-title>新增商城活动</q-toolbar-title>
              <q-btn flat @click="drawerR = !drawerR" round dense icon="menu" />
              <q-btn flat v-close-popup round dense icon="close" @click="add_sales_activity_layout = false" />
            </q-toolbar>
          </q-header>

          <q-page-container>
            <q-page padding>
              <q-form ref="myForm" @submit="submitNewSalesActivity" @reset="onReset" class="q-gutter-md"
                style="margin-left: 2%;">
                <br>
                <q-input color="primary" v-model="sales_demand_price" label="达到满减要求的金额" lazy-rules :rules="[
                  val => val && /^\d+(\.\d{1,2})?$/.test(val) || '请填写活动要满足的金额',
                  val => val && DemandPriceJudge(val) || '金额太少',
                ]" style="margin-left: 5%; width: 90%;" />

                <q-input color="primary" v-model="sales_decrease_price" label="可减的金额" lazy-rules :rules="[
                  val => val && /^\d+(\.\d{1,2})?$/.test(val) || '请输入可减的金额',
                  val => val && DecreasePriceUpperBound(val) || '输出太多',
                ]" style="margin-left: 5%; width: 90%;" />

                <q-input color="primary" v-model="sales_fund" label="商城补贴金额" lazy-rules :rules="[
                  val => val && /^\d+(\.\d{1,2})?$/.test(val) || '请输入补贴金额',
                  val => val && SalesFundUpperBound(val) || '超出商城账户余额',
                ]" style="margin-left: 5%; width: 90%;" />

                <q-input color="primary" v-model="shop_register_fund" label="商家注册资金下限（元）" lazy-rules :rules="[
                  val => val && /^\d+(\.\d{1,2})?$/.test(val) || '请输入商家注册资金下限（元）',
                ]" style="margin-left: 5%; width: 90%;" />

                <q-input color="primary" v-model="shop_selling_amount" label="店铺月销量下限（件）" lazy-rules :rules="[
                  val => val && /^\d+(\.\d{1,2})?$/.test(val) || '请输入店铺月销量下限（件）',
                ]" style="margin-left: 5%; width: 90%;" />

                <q-input color="primary" v-model="shop_monthly_profit" label="店铺月销售额下限（元）" lazy-rules :rules="[
                  val => val && /^\d+(\.\d{1,2})?$/.test(val) || '请输入店铺月销售额下限（元）',
                ]" style="margin-left: 5%; width: 90%;" />

                <div class="q-px-sm" style="font-size: large;" color="grey-10">
                  可参与活动的商品：
                </div>
                <!-- TODO: 根据后端数据改绑定 -->
                <div class="goods-category" v-for="category in goods_category_lst" :key="category"
                  style="display: inline-block;">
                  <q-checkbox v-model="selection" :label="category" :val="category" />
                </div>

                <q-input filled v-model="activity_start_date" :rules="[]" style="width: 90%;" label="活动开始时间 *">
                  <template v-slot:append>
                    <q-icon name="event" class="cursor-pointer">
                      <q-popup-proxy cover transition-show="scale" transition-hide="scale">
                        <q-date v-model="activity_start_date" mask="YYYY-MM-DD">
                          <div class="row items-center justify-end">
                            <q-btn v-close-popup label="Close" color="primary" flat />
                          </div>
                        </q-date>
                      </q-popup-proxy>
                    </q-icon>
                  </template>
                </q-input>

                <q-input filled v-model="activity_end_date" :rules="[]" style="width: 90%;" label="活动结束时间 *">
                  <template v-slot:append>
                    <q-icon name="event" class="cursor-pointer">
                      <q-popup-proxy cover transition-show="scale" transition-hide="scale">
                        <q-date v-model="activity_end_date" mask="YYYY-MM-DD">
                          <div class="row items-center justify-end">
                            <q-btn v-close-popup label="Close" color="primary" flat />
                          </div>
                        </q-date>
                      </q-popup-proxy>
                    </q-icon>
                  </template>
                </q-input>

                <div class="submit">
                  <q-btn label="发布活动" type="submit" color="red-4" />
                  <q-btn label="重置" type="reset" color="red-4" flat class="q-ml-sm" />
                </div>
              </q-form>
            </q-page>
          </q-page-container>
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

    </q-page-container>
  </q-layout>
</template>


<script setup>
import { ref } from 'vue'
import axios from 'axios'
import { onMounted } from 'vue'
import { useQuasar } from 'quasar'

const q = useQuasar();
const axiosInstance = axios.create({
  baseURL: 'http://120.46.154.28:9999',
});

// 操作成功or失败的提示
let operation_success_alert_layout = ref(false)
let operation_success_message = ref(null)
let operation_fail_alert_layout = ref(false)
let operation_fail_message = ref(null)

let leftDrawerOpen = ref(false)
let showTransition = ref(false)     // 中间账户
let showAllApplication = ref(true)  // 管理员默认页面
let showProfit = ref(false)         // 利润账户
let showSalesActivity = ref(false)  // 商城活动
let showApprovedApps = ref(false)
let showRejectedApps = ref(false)
let showRemainCheckedApps = ref(false)
const username = ref('admin')
let selected = ref([])
let errorMsg = ref(null)
let response_register = ref(null)
let intermediateAccount = ref(0)
let profitAccount = ref(0)


// 商城活动
let show_activity_list = ref(true)
let show_activity_application_list = ref(false)

let all_activities = ref([])  // 所有活动
let rc_activity_app = ref([]) // 为批准的参与活动申请
let a_activity_app = ref([])  // 已批准的参与活动申请
let r_activity_app = ref([])  // 已拒绝的参与活动申请
let add_sales_activity_layout = ref(false)
let sales_demand_price = ref(null)
let sales_decrease_price = ref(null)
let sales_fund = ref(null)
let activity_start_date = ref(null)
let activity_end_date = ref(null)
let shop_register_fund = ref(null)
let shop_selling_amount = ref(null)
let shop_monthly_profit = ref(null)

let current_time = ref(null)


// 商品类别
// TODO：改成后端给我的category列表
let goods_category_lst = ref([])
let selection = ref([])

// 已同意的商店申请
let aShopAppColumns = [
  {
    name: 'applicationType',
    required: true,
    label: '申请类型',
    align: 'left',
    field: row => row.status,
    format: val => `${val}`,
    sortable: false
  },
  {
    name: 'shopName',
    required: true,
    label: '店铺名称',
    align: 'left',
    field: row => row.shopName,
    format: val => `${val}`,
    sortable: false
  },
  {
    name: 'category',
    required: true,
    label: '商品类别',
    align: 'left',
    field: row => row.category,
    format: val => {
      if (Array.isArray(val)) {
        return val.join(', ')
      } else if (typeof val === 'object' && val !== null && val.id && val.text) {
        return `${val.id} - ${val.text}`
      } else {
        return ''
      }
    },
    sortable: true
  },
  { name: 'idNumber', label: '申请人身份证号', field: 'idNumber' },
  { name: 'introduce', label: '商店简介', field: 'introduce' },
  { name: 'address', label: '备案地址', field: 'address' },
  { name: 'fund', label: '注册资金', field: 'fund', sortable: true, sort: (a, b) => parseFloat(a, 10) - parseFloat(b, 10) },
  { name: 'registrationTime', label: '申请时间 YYYY-MM-DD', field: 'registrationTime', sortable: false },
]
let aShopAppRows = ref([])

// 已同意的商品申请
// TODO: 修改商品申请的信息
let aCommodityUpApp = ref([])
let aCommodityEditApp = ref([])

// 已拒绝的商店申请
let rShopAppColumns = [
  {
    name: 'applicationType',
    required: true,
    label: '申请类型',
    align: 'left',
    field: row => row.status,
    format: val => `${val}`,
    sortable: false
  },
  {
    name: 'shopName',
    required: true,
    label: '店铺名称',
    align: 'left',
    field: row => row.shopName,
    format: val => `${val}`,
    sortable: false
  },
  {
    name: 'category',
    required: true,
    label: '商品类别',
    align: 'left',
    field: row => row.category,
    format: val => {
      if (Array.isArray(val)) {
        return val.join(', ')
      } else if (typeof val === 'object' && val !== null && val.id && val.text) {
        return `${val.id} - ${val.text}`
      } else {
        return ''
      }
    },
    sortable: true
  },
  { name: 'idNumber', label: '申请人身份证号', field: 'idNumber' },
  { name: 'introduce', label: '商店简介', field: 'introduce' },
  { name: 'address', label: '备案地址', field: 'address' },
  { name: 'fund', label: '注册资金', field: 'fund', sortable: true, sort: (a, b) => parseFloat(a, 10) - parseFloat(b, 10) },
  { name: 'registrationTime', label: '申请时间 YYYY-MM-DD', field: 'registrationTime', sortable: false },
]
let rShopAppRows = ref([])

// 已拒绝的商品申请
let rCommodityUpApp = ref([])
let rCommodityEditApp = ref([])

// 待查看的店铺申请
let rcShopAppColumns = [
  {
    name: 'applicationType',
    required: true,
    label: '申请类型',
    align: 'left',
    field: row => row.status,
    format: val => `${val}`,
    sortable: false
  },
  {
    name: 'shopName',
    required: true,
    label: '店铺名称',
    align: 'left',
    field: row => row.shopName,
    format: val => `${val}`,
    sortable: false
  },
  {
    name: 'category',
    required: true,
    label: '商品类别',
    align: 'left',
    field: row => row.category,
    format: val => {
      if (Array.isArray(val)) {
        return val.join(', ')
      } else if (typeof val === 'object' && val !== null && val.id && val.text) {
        return `${val.id} - ${val.text}`
      } else {
        return ''
      }
    },
    sortable: true
  },
  { name: 'idNumber', label: '申请人身份证号', field: 'idNumber' },
  { name: 'introduce', label: '商店简介', field: 'introduce' },
  { name: 'address', label: '备案地址', field: 'address' },
  { name: 'fund', label: '注册资金', field: 'fund', sortable: true, sort: (a, b) => parseFloat(a, 10) - parseFloat(b, 10) },
  { name: 'registrationTime', label: '申请时间 YYYY-MM-DD', field: 'registrationTime', sortable: false },
  {
    name: 'action',
    label: '同意申请',
    field: 'action',
    align: 'center',
    sortable: false
  },
]
let rcShopAppRows = ref([])

// 待查看的商品申请
let rcCommodityUpApp = ref([])
let rcCommodityEditApp = ref([])

// 商城中间账户和利润账户余额
let profitExpanded = ref(false)
let intermediateExpanded = ref(false)

let transaction_range = ref('all_transactions')

// 中间账户&利润账户流水
let TransitionColumn = [
  { name: 'transferName', label: '转账人', field: 'transferName' },
  { name: 'receiveName', label: '收款人', field: 'receiveName' },
  { name: 'amount', label: '金额（¥）', field: 'amount' },
  { name: 'time', label: '交易时间', field: 'time' },
  { name: 'comments', label: '备注', field: 'comments' },

]
let intermediateAccountTrans = ref([])
let profitAccountTrans = ref([])

onMounted(() => {
  // 获取所有待审批的商店信息：开店/闭店
  getRcShop()

  // 获取所有通过审批的商店信息：开店/闭店
  getAShop()

  // 获取所有已拒绝审批的商店信息：开店/闭店
  getRShop()

  // 请求所有待审批上架的商品信息
  getComAddApply()

  // 请求所有待审批修改信息的商品信息
  getComEditApply()

  // 请求所有已通过上架的商品信息
  getComAddApproved()

  // 请求所有已通过修改信息的商品信息
  getComEditApproved()

  // 请求所有已驳回上架的商品信息
  getComUpRejected()

  // 请求所有已驳回修改信息的商品信息
  getComEditRejected()

  // 获取商城中间账号信息
  axiosInstance.post('/admin/getIntermediateAccount').then((response) => {
    const r = response.data['data']
    console.log('get intermediate account msg: ', r);
    intermediateAccount.value = r
  });

  // 获取商城利润账户信息
  axiosInstance.post('/admin/getProfitAccount').then((response) => {
    const r = response.data['data']
    console.log('get profile account msg: ', r);
    profitAccount.value = r
  });

  // 获取所有商品种类
  getAllCategories()

  // 获取商城中间账户和利润账户的流水
  getTransfer()

  getAllSalesActivity()
  getRcSalesActivityApp()
  getASalesActivityApp()
  getRSalesActivityApp()
})

// 左侧栏选择渲染
function showAdminMainPage() {
  showTransition.value = false
  showAllApplication.value = true
  showProfit.value = false
  showSalesActivity.value = false
}

function showTransitionMode() {
  showTransition.value = true
  showAllApplication.value = false
  showProfit.value = false
  showSalesActivity.value = false
}

function showProfitMode() {
  showProfit.value = true
  showAllApplication.value = false
  showTransition.value = false
  showSalesActivity.value = false
}

function showAllSalesActivity() {
  showProfit.value = false
  showAllApplication.value = false
  showTransition.value = false
  showSalesActivity.value = true
}

function selectRemainChecked() {
  showApprovedApps.value = false
  showRejectedApps.value = false
  showRemainCheckedApps.value = true
}

function selectApproved() {
  showApprovedApps.value = true
  showRejectedApps.value = false
  showRemainCheckedApps.value = false
}

function selectRejected() {
  showApprovedApps.value = false
  showRejectedApps.value = true
  showRemainCheckedApps.value = false
}

function showActivitysList() {
  show_activity_list.value = true
  show_activity_application_list.value = false
  getAllSalesActivity()
}

function showActivityApplicationList() {
  show_activity_list.value = false
  show_activity_application_list.value = true
}

function DemandPriceJudge(price) {
  return (price >= 100)
}

function DecreasePriceUpperBound(price) {
  return (price <= sales_demand_price.value / 2)
}

function SalesFundUpperBound(fund) {
  return (fund <= profitAccount.value)
}

// 获取当前日期 YYYY-MM-DD
function getFormattedDate() {
  const now = new Date();
  const isoDate = now.toISOString();
  return isoDate.substr(0, 10);
}

// 获取所有商品种类
function getAllCategories() {
  axiosInstance.post('/shopCategory/getCategory', {}).then((response) => {
    const r = response.data['data']
    console.log('get all categories: ', r);
    r.forEach(function (item) {
      goods_category_lst.value.push(item)
    });
  });
  console.log("goods category list: ", goods_category_lst.value)
}

// 获取所有待审批的商店信息：开店/闭店
function getRcShop() {
  axiosInstance.post('/shop/showByStatus0_3').then((response) => {
    const r = response.data['data']
    console.log('get rc shop msg: ', r);
    r.forEach(function (item) {
      // console.log(item.id)
      // item.id is shopId!
      item.shopId = item.id;
      delete item.id
    });
    r.map(obj => {
      const [year, month, day] = obj.registrationTime;
      obj.registrationTime = `${year}-${month}-${day}`;
      switch (obj.status) {
        case 0:
          obj.status = '申请开店';
          break;
        case 3:
          obj.status = '申请闭店';
          break;
        default:
          break;
      }
      return obj;
    });
    rcShopAppRows.value.splice(0, rcShopAppRows.value.length, ...r);
    console.log('rc shop value: ', rcShopAppRows.value)
  });
}

// 获取所有通过审批的商店信息：开店/闭店
function getAShop() {
  axiosInstance.post('/shop/showByStatus1_4').then((response) => {
    const r = response.data['data']
    console.log('get approved shop msg: ', r);
    r.forEach(function (item) {
      // console.log(item.id)
      // item.id is shopId!
      item.shopId = item.id;
      delete item.id
    });
    r.map(obj => {
      const [year, month, day] = obj.registrationTime;
      obj.registrationTime = `${year}-${month}-${day}`;
      switch (obj.status) {
        case 1:
          obj.status = '申请开店';
          break;
        case 4:
          obj.status = '申请闭店';
          break;
        default:
          break;
      }
      return obj;
    });
    aShopAppRows.value.splice(0, aShopAppRows.value.length, ...r);
    console.log(aShopAppRows.value)
  });
}

// 获取所有已拒绝审批的商店信息：开店/闭店
function getRShop() {
  axiosInstance.post('/shop/showByStatus2_5').then((response) => {
    const r = response.data['data']
    console.log('get delete shop msg: ', r);
    r.forEach(function (item) {
      // console.log(item.id)
      // item.id is shopId!
      item.shopId = item.id;
      delete item.id
    });
    r.map(obj => {
      const [year, month, day] = obj.registrationTime;
      obj.registrationTime = `${year}-${month}-${day}`;
      switch (obj.status) {
        case 2:
          obj.status = '申请开店';
          break;
        case 5:
          obj.status = '申请闭店';
          break;
        default:
          break;
      }
      return obj;
    });
    rShopAppRows.value.splice(0, rShopAppRows.value.length, ...r);
    console.log(rShopAppRows.value)
  });
}

// 请求所有待审批上架的商品信息
function getComAddApply() {
  axiosInstance.get('/Goods/showAddApply').then((response) => {
    const r = response.data['data']
    console.log('get 待审批上架商品 msg: ', r);
    r.forEach(function (item) {
      if (item !== null) {
        rcCommodityUpApp.value.push(item)
      }
    });
    console.log("申请待审批上架商品后：", rcCommodityUpApp.value)
  });
}

// 请求所有待审批修改信息的商品信息
function getComEditApply() {
  axiosInstance.get('/Goods/showUpdateApply').then((response) => {
    const r = response.data['data']
    console.log('get 待审批修改商品 msg: ', r);
    r.forEach(function (item) {
      if (item !== null) {
        rcCommodityEditApp.value.push(item)
      }
    });
    // rcCommodityEditApp.value.push(0, rcCommodityEditApp.value.length, ...r);
    console.log("申请待审批修改商品后：", rcCommodityEditApp.value)
  });
}

// 请求所有已通过上架的商品信息
function getComAddApproved() {
  axiosInstance.get('/Goods/showAddApproved').then((response) => {
    const r = response.data['data']
    console.log('get 已通过上架商品 msg: ', r);
    r.forEach(function (item) {
      if (item !== null) {
        aCommodityUpApp.value.push(item)
      }
    });
    // aCommodityUpApp.value.splice(0, aCommodityUpApp.value.length, ...r);
    console.log("已通过上架商品后：", aCommodityUpApp.value)
  });
}

// 请求所有已通过修改信息的商品信息
function getComEditApproved() {
  axiosInstance.get('/Goods/showUpdateApproved').then((response) => {
    const r = response.data['data']
    console.log('get 已通过修改商品 msg: ', r);
    r.forEach(function (item) {
      if (item !== null) {
        aCommodityUpApp.value.push(item)
      }
    });
    // aCommodityUpApp.value.splice(0, aCommodityUpApp.value.length, ...r);
    console.log("申请已通过修改商品后：", aCommodityEditApp.value)
  });
}

// 请求所有已驳回上架的商品信息
function getComUpRejected() {
  axiosInstance.get('/Goods/showAddRejected').then((response) => {
    const r = response.data['data']
    console.log('get 已驳回上架商品 msg: ', r);
    r.forEach(function (item) {
      if (item !== null) {
        rCommodityUpApp.value.push(item)
      }
    });
    // rCommodityUpApp.value.splice(0, aCommodityUpApp.value.length, ...r);
    console.log("已驳回上架商品后：", rCommodityUpApp.value)
  });
}

// 请求所有已驳回修改信息的商品信息
function getComEditRejected() {
  axiosInstance.get('/Goods/showUpdateRejected').then((response) => {
    const r = response.data['data']
    console.log('get 已驳回修改商品 msg: ', r);
    r.forEach(function (item) {
      if (item !== null) {
        rCommodityUpApp.value.push(item)
      }
    });
    // rCommodityUpApp.value.splice(0, aCommodityUpApp.value.length, ...r);
    console.log("申请已驳回修改商品后：", rCommodityEditApp.value)
  });
}

// 获取商城中间商户和商城利润账户的流水
function getTransfer() {
  profitAccountTrans.value = []
  intermediateAccountTrans.value = []
  console.log("current transaction range: ", transaction_range.value)
  if (transaction_range.value === 'all_transactions') {
    axiosInstance.post('/transferRecords/adminIntermediate', {}).then((response) => {
      const r = response.data['data']
      console.log('get all admin intermediate transfer records: ', r);
      r.forEach(function (item) {
        const [year, month, day] = item.time;
        item.time = `${year}-${month}-${day}`;
        console.log(item);
        intermediateAccountTrans.value.push(item)
      });
    });
    axiosInstance.post('/transferRecords/adminProfit', {}).then((response) => {
      const r = response.data[1]['data']
      console.log('get all admin profit transfer records: ', r);
      r.forEach(function (item) {
        const [year, month, day] = item.time;
        item.time = `${year}-${month}-${day}`;
        profitAccountTrans.value.push(item)
      });
    });
  } else if (transaction_range.value === 'last_month_transactions') {
    axiosInstance.post('/transferRecords/adminIntermediateWithinOneMonth', {}).then((response) => {
      const r = response.data['data']
      console.log('get admin intermediate transfer records in last MONTH: ', r);
      r.forEach(function (item) {
        const [year, month, day] = item.time;
        item.time = `${year}-${month}-${day}`;
        console.log(item);
        intermediateAccountTrans.value.push(item)
      });
    });
    axiosInstance.post('/transferRecords/adminProfitWithinOneMonth', {}).then((response) => {
      const r = response.data[1]['data']
      console.log('get admin profit transfer records in last MONTH: ', r);
      r.forEach(function (item) {
        const [year, month, day] = item.time;
        item.time = `${year}-${month}-${day}`;
        profitAccountTrans.value.push(item)
      });
    });
  } else if (transaction_range.value === 'last_week_transactions') {
    axiosInstance.post('/transferRecords/adminIntermediateWithinOneWeek', {}).then((response) => {
      const r = response.data['data']
      console.log('get admin intermediate transfer records in last WEEK: ', r);
      r.forEach(function (item) {
        const [year, month, day] = item.time;
        item.time = `${year}-${month}-${day}`;
        console.log(item);
        intermediateAccountTrans.value.push(item)
      });
    });
    axiosInstance.post('/transferRecords/adminProfitWithinOneWeek', {}).then((response) => {
      const r = response.data[1]['data']
      console.log('get admin profit transfer records in last WEEK: ', r);
      r.forEach(function (item) {
        const [year, month, day] = item.time;
        item.time = `${year}-${month}-${day}`;
        profitAccountTrans.value.push(item)
      });
    });
  }
}

// 同意申请
function approveApplication(rcShopAppRow) {
  // 根据行的id执行同意申请操作
  current_time.value = getFormattedDate()
  console.log('同意申请传参: shop id: ', rcShopAppRow.shopId, '\nuser id: ', rcShopAppRow.userId,
    '\nfund: ', rcShopAppRow.fund, '\ncurrent date: ', current_time.value)
  console.log("type of cur_time: ", typeof current_time.value)
  if (rcShopAppRow.status === '申请开店') {
    axiosInstance.put(`/shop/approve?stime=${current_time.value}`, {
      id: rcShopAppRow.shopId,
      userId: rcShopAppRow.userId,
      fund: rcShopAppRow.fund,
    }).then((response) => {
      const r = response.data['data']
      console.log('开店申请response data: ', r);
    }).catch((error) => {
      console.error('Error:', error);
      // 处理错误
      // errorMsg.value = response_register.value['message']
    });
  }
  else if (rcShopAppRow.status === '申请闭店') {
    axiosInstance.post(`/shop/approveDelete?stime=${current_time.value}`, {
      id: rcShopAppRow.shopId,
      account: rcShopAppRow.account,
      userId: rcShopAppRow.userId,
    }).then((response) => {
      const r = response.data['data']
      console.log(r);
    }).catch((error) => {
      console.error('Error:', error);
      // 处理错误
    });
  }
  axiosInstance.post('/shop/showByStatus1_4').then((response) => {
    const r = response.data['data']
    console.log('get approved shop msg: ', r);
    r.forEach(function (item) {
      // console.log(item.id)
      // item.id is shopId!
      item.shopId = item.id;
      delete item.id
    });
    r.map(obj => {
      const [year, month, day] = obj.registrationTime;
      obj.registrationTime = `${year}-${month}-${day}`;
      switch (obj.status) {
        case 1:
          obj.status = '申请开店';
          break;
        case 4:
          obj.status = '申请闭店';
          break;
        default:
          break;
      }
      return obj;
    });
    aShopAppRows.value.splice(0, aShopAppRows.value.length, ...r);
    console.log(aShopAppRows.value)
    update()
  });
}

function disapproveApplication(rcShopAppRow) {
  // 根据行的id执行同意申请操作
  console.log('拒绝申请传参: ', rcShopAppRow)
  if (rcShopAppRow.status === '申请开店') {
    axiosInstance.put('/shop/disapprove', {
      id: rcShopAppRow.shopId,
      userId: rcShopAppRow.userId,
      fund: rcShopAppRow.fund,
    }).then((response) => {
      const r = response.data['data']
      console.log('开店申请response data: ', r);
    }).catch((error) => {
      console.error('Error:', error);
      // 处理错误
      errorMsg.value = response_register.value['message']
    });
  }
  else if (rcShopAppRow.status === '申请闭店') {
    axiosInstance.post('/shop/disapproveDelete', {
      id: rcShopAppRow.shopId,
      account: rcShopAppRow.account,
      userId: rcShopAppRow.userId,
    }).then((response) => {
      const r = response.data['data']
      console.log(r);
      update()
    }).catch((error) => {
      console.error('Error:', error);
      // 处理错误
    });
  }
}

function approveCommodityUpdate(good) {
  console.log('同意商品上架传参：', good.id)
  axiosInstance.put('/Goods/addGoodsApprove', {
    goodsId: good.id,
  }, {
    params: {
      goodsId: good.id,
    }
  }).then((response) => {
    const r = response.data['data']
    console.log('get 同意商品上架 msg: ', r);
    update()
  });
}


function disapproveCommodityUpdate(good) {
  console.log('不同意商品上架传参：', good, '\nid: ', good.id)
  axiosInstance.put('/Goods/addGoodsReject', {
    goodsId: good.id,
  }, {
    params: {
      goodsId: good.id,
    }
  }).then((response) => {
    const r = response.data['data']
    console.log('get 不同意商品上架 msg: ', r);
    update()
  });
}

function approveCommodityEdit(good) {
  console.log('同意商品修改传参：', good.id)
  axiosInstance.put('/Goods/updateGoodsApprove', {
    goodsId: good.id,
  }, {
    params: {
      goodsId: good.id,
    }
  }).then((response) => {
    const r = response.data['data']
    console.log('get 同意商品修改 msg: ', r);
    update()
  });
}

function disapproveCommodityEdit(good) {
  console.log('不同意商品修改传参：', good.id)
  axiosInstance.put('/Goods/updateGoodsReject', {
    goodsId: good.id,
  }, {
    params: {
      goodsId: good.id,
    }
  }).then((response) => {
    const r = response.data['data']
    console.log('get 不同意商品修改 msg: ', r);
    update()
  });
}

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


// 刷新button事件
function update() {
  aShopAppRows.value = []
  rShopAppRows.value = []
  rcShopAppRows.value = []

  aCommodityUpApp.value = []
  aCommodityEditApp.value = []

  rCommodityUpApp.value = []
  rCommodityEditApp.value = []

  rcCommodityUpApp.value = []
  rcCommodityEditApp.value = []

  // 获取所有待审批的商店信息：开店/闭店
  getRcShop()

  // 获取所有通过审批的商店信息：开店/闭店
  getAShop()

  // 获取所有已拒绝审批的商店信息：开店/闭店
  getRShop()

  // 请求所有待审批上架的商品信息
  getComAddApply()

  // 请求所有待审批修改信息的商品信息
  getComEditApply()

  // 请求所有已通过上架的商品信息
  getComAddApproved()

  // 请求所有已通过修改信息的商品信息
  getComEditApproved()

  // 请求所有已驳回上架的商品信息
  getComUpRejected()

  // 请求所有已驳回修改信息的商品信息
  getComEditRejected()

  // 获取商城中间账号信息
  axiosInstance.post('/admin/getIntermediateAccount').then((response) => {
    const r = response.data['data']
    console.log('get intermediate account msg: ', r);
    intermediateAccount.value = r
  });

  // 获取商城利润账户信息
  axiosInstance.post('/admin/getProfitAccount').then((response) => {
    const r = response.data['data']
    console.log('get profile account msg: ', r);
    profitAccount.value = r
  });

  //获取流水
  getTransfer()

  // 获取商城活动申请
  getAllSalesActivity()
  getRcSalesActivityApp()
  getASalesActivityApp()
  getRSalesActivityApp()
}

function submitNewSalesActivity() {
  current_time.value = getFormattedDate()
  console.log(
    '满', sales_demand_price.value,
    '减', sales_decrease_price.value,
    '\n商品类别', selection.value,
    '\n起始日期', activity_start_date.value,
    '\n结束日期', activity_end_date.value,
    '\nsdate', current_time.value,
  )
  axiosInstance.post('/event/openEvent', {
    amount: sales_demand_price.value,
    category: selection.value,
    discount: sales_decrease_price.value,
    endDate: activity_end_date.value,
    funds: sales_fund.value,
    shopFund: shop_register_fund.value, 
    shopProfit: shop_monthly_profit.value, 
    shopSales: shop_selling_amount.value, 
    startDate: activity_start_date.value,
  }, {
    params: {
      sdate: current_time.value,
    }
  }).then((response) => {
    const r = response.data
    console.log("新活动返回：", r)
    if (r.code === 20000) {
      operation_success_alert_layout.value = true
      operation_success_message.value = '新增活动成功'
    } else {
      operation_fail_alert_layout.value = true
      operation_fail_message.value = r.message
    }
    add_sales_activity_layout.value = false
  });
}

function getRcSalesActivityApp() {
  rc_activity_app.value = []
  axiosInstance.get('/eventApply/getApplying').then((response) => {
    const r = response.data['data']
    console.log('待批准的活动申请: ', r);
    r.forEach(function (item) {
      if (item != null && (item.event.status === 1 || (item.event.status === 0 && item.event.id !== 0))) {
        const [s_year, s_month, s_day] = item.event.startDate;
        item.event.startDate = `${s_year}年${s_month}月${s_day}日`;
        const [e_year, e_month, e_day] = item.event.endDate;
        item.event.endDate = `${e_year}年${e_month}月${e_day}日`;
        rc_activity_app.value.push(item)
      }
    });
  });
  console.log("待审批的活动申请：", rc_activity_app.value)
}

function getASalesActivityApp() {
  a_activity_app.value = []
  axiosInstance.get('/eventApply/getApproved').then((response) => {
    const r = response.data['data']
    console.log('已同意的活动申请: ', r);
    r.forEach(function (item) {
      if (r != null) {
        const [s_year, s_month, s_day] = item.event.startDate;
        item.event.startDate = `${s_year}年${s_month}月${s_day}日`;
        const [e_year, e_month, e_day] = item.event.endDate;
        item.event.endDate = `${e_year}年${e_month}月${e_day}日`;
        a_activity_app.value.push(item)
      }
    });
  });
}

function getRSalesActivityApp() {
  r_activity_app.value = []
  axiosInstance.get('/eventApply/getRejected').then((response) => {
    const r = response.data['data']
    console.log('已拒绝的活动申请: ', r);
    r.forEach(function (item) {
      if (r != null) {
        const [s_year, s_month, s_day] = item.event.startDate;
        item.event.startDate = `${s_year}年${s_month}月${s_day}日`;
        const [e_year, e_month, e_day] = item.event.endDate;
        item.event.endDate = `${e_year}年${e_month}月${e_day}日`;
        r_activity_app.value.push(item)
      }
    });
  });
}

function approveSalesActivityApp(activity_application) {
  console.log('同意参加活动修改传参：', activity_application.eventApply.id)
  axiosInstance.put('/event/approveApply', {
    applyId: activity_application.eventApply.id,
  }, {
    params: {
      applyId: activity_application.eventApply.id,
    }
  }).then((response) => {
    const r = response.data
    console.log('同意参加活动反馈: ', r);
    update()
  });
}

function disapproveSalesActivityApp(activity_application) {
  console.log('不同意参加活动修改传参：', activity_application.eventApply.id)
  axiosInstance.put('/event/disapproveApply', {
    applyId: activity_application.eventApply.id,
  }, {
    params: {
      applyId: activity_application.eventApply.id,
    }
  }).then((response) => {
    const r = response.data
    console.log('不同意参加活动反馈: ', r);
    update()
  });
}

function toggleLeftDrawer() {
  leftDrawerOpen.value = !leftDrawerOpen.value
}

// 深色模式
function toggle() {
  q.dark.toggle();
}


// ？
function getSelectedString() {
  return selected.value.length === 0 ? '' : `${selected.value.length} record${selected.value.length > 1 ? 's' : ''} selected of ${rows.value.length}`
}
</script>

<style>
.router-link-class {
  color: inherit;
  text-decoration: none;
}
</style>
