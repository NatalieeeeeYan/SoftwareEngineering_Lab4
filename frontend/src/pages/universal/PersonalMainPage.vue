<template>
  <q-layout view="hHh lpR fFf">

    <q-header elevated class="text-grey-8" height-hint="64">
      <q-toolbar class="GNL__toolbar">
        <q-btn flat dense round @click="toggleLeftDrawer" aria-label="Menu" icon="menu" class="q-mr-sm" />

        <q-toolbar-title v-if="$q.screen.gt.xs" shrink class="row items-center no-wrap">
          <!-- <img src="https://cdn.quasar.dev/img/layout-gallery/logo-google.svg"> -->
          <span class="q-ml-sm">{{ headerLabel }}</span>
        </q-toolbar-title>

        <q-item clickable target="_blank" rel="noopener" @click="toggle">
          <q-item-section>Dark Mode</q-item-section>
        </q-item>

        <q-space />

        <div class="q-gutter-sm row items-center no-wrap">
          <q-btn round flat>
            <q-avatar size="26px">
              <!-- 待修改：用户头像（和商品图片同样流程从数据库里抽取） -->
              <img :src=Imgurl>
            </q-avatar>
            <q-tooltip style="position: center;">
              {{ username }}
            </q-tooltip>
            <q-space />
          </q-btn>
        </div>

      </q-toolbar>
    </q-header>

    <!-- 左侧栏 -->
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
              返回首页
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
            <!-- TODO: 完善说明页面，并添加链接 -->
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

    <q-page-container style="display: flex; margin-left: 2%;">
      <!-- 账户个人信息 -->
      <div v-if="showAccountInfo" style="width: 100%;">
        <h5
          style="margin-left: 1%; line-height: 5px; font-weight: 500; font-size: 30px; font-family: system-ui, -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen, Ubuntu, Cantarell, 'Open Sans', 'Helvetica Neue', sans-serif;">
          Public profile</h5>

        <q-separator inset class="q-my-sm" style="width: 98%; margin-left: 1%;" />

        <div class="profile-display-box justify-between">

          <div class="personalInfo-display-change" style="max-width: 100%; width: 70%;">
            <div class="textInfo" style="max-width: 100%; width: 100%;">
              <q-form ref="myForm" action="http://120.46.154.28:9999/user" method="post" @submit="postProfileUpdate"
                @reset="onReset" class="q-gutter-md">
                <dl>
                  <dt>
                    <label class="label" for="userName">Name</label>
                  </dt>
                  <dd>
                    <q-input filled name="username" v-model="username" hint="您在本网站显示的昵称" :rules="[
                      (val) => (val && val.length > 0) || '请输入您的昵称',
                      (val) => code != 20004 || '该昵称已存在',
                    ]" @click="code = 0" :disable="viewOnly" />
                  </dd>
                </dl>

                <dl>
                  <dt>
                    <label class="label" for="userPhone">Phone No.</label>
                  </dt>
                  <dd>
                    <q-input filled name="phone" v-model="phone" v-model:model-value="phone" hint="您绑定的手机号" lazy-rules
                      :rules="[
                        (val) => (val && val.length > 0) || '请输入您的手机号',
                        (val) => val.length === 11 || '请输入正确的手机号',
                        (val) => code != 20005 || '手机已被注册过',
                      ]" @click="code = 0" :disable="viewOnly" />
                  </dd>
                </dl>

                <dl>
                  <dt>ID Card No.</dt>
                  <dd>
                    <q-input filled name="idNumber" v-model="idNumber" hint="您绑定的身份证号（不可修改）" disable />
                  </dd>
                </dl>

                <dl>
                  <dt>
                    <label class="label" for="userEmail">Public email</label>
                  </dt>
                  <dd>
                    <q-input filled name="email" v-model="email" hint="您绑定的电子邮箱" lazy-rules :rules="[
                      (val) => (val && val.length > 0) || '请输入您的邮箱地址',
                      (val) => validateEmailDomain(val) || 'Invalid email domain',
                      (val) => /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/.test(val) || 'Invalid email',
                      code != 20007 || '邮箱已被注册过',
                    ]" @click="code = 0" :disable="viewOnly" />
                  </dd>
                </dl>

                <div class="button-box">
                  <div v-if="viewOnly" class="edit-button">
                    <q-btn color="secondary" label="修改信息" @click="viewEditModeChange" />
                  </div>

                  <div v-if="!viewOnly" class="submit-button">
                    <q-btn color="secondary" label="提交修改" @click="postProfileUpdate" />
                  </div>
                  <q-tooltip v-if="editNotification" anchor="bottom middle" self="top middle" :offset="[10, 10]">
                    <strong>{{ editMsg }}</strong>
                    (<q-icon name="keyboard_arrow_down" />)
                  </q-tooltip>
                </div>

              </q-form>

            </div>
          </div>

          <div class="profilePicture">
            <dl>
              <dt>
                <label class="label" for="userName">Profile picture</label>
              </dt>
              <dl>
                <div class="profile-img">
                  <q-img :src="Imgurl" style="border-radius: 50%;" />
                </div>
                <div class="profile-edit">
                  <q-btn text-color="#000" icon="edit" label="Edit" border-radius="6px"
                    style="background: #fffbf0; color: black" />
                </div>
              </dl>
            </dl>
          </div>
        </div>
      </div>

      <!-- 账户资金信息 -->
      <div v-if="showAccountAmount" style="width: 100%; ">
        <div class="q-pa-md row items-start q-gutter-md" style="min-width: 800px; margin-left: 5%; margin-top: 5%;">
          <q-card class="my-card" flat bordered style="min-width: 700px;">

            <q-card-section>
              <div class="text-overline text-orange-9">Wallet</div>
              <div class="text-h5 q-mt-sm q-mb-xs">余额：¥{{ balance }}</div>
            </q-card-section>

            <q-card-actions>
              <q-btn flat color="white" text-color="black" label="充值" @click="showTopUpDialogue" />
              <q-dialog v-model="show_top_up" persistent>
                <q-card style="min-width: 350px">
                  <q-card-section>
                    <div class="text-h6">充值</div>
                  </q-card-section>

                  <q-card-section class="q-pt-none">
                    <q-input dense v-model="money" autofocus @keyup.enter="topUpAccount()" />
                  </q-card-section>

                  <q-card-actions align="right" class="text-secondary">
                    <q-btn flat label="取消" v-close-popup />
                    <q-btn flat label="确认" v-close-popup @click="topUpAccount()" />
                  </q-card-actions>
                </q-card>
              </q-dialog>

              <q-space />

              <q-btn color="grey" round flat dense :icon="expanded ? 'keyboard_arrow_up' : 'keyboard_arrow_down'"
                @click="expanded = !expanded" />
            </q-card-actions>

            <q-slide-transition>
              <div v-show="expanded">
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
                <q-table :rows="transitionRows" :columns="transitionCols" row-key="transferName"
                  style="margin-top: 2%; margin-left: 1%;" />
              </div>
            </q-slide-transition>
          </q-card>

          <q-dialog v-model="layout" width>
            <q-layout view="Lhh lpR fff" container class="bg-white">
              <q-header class="bg-primary">
                <q-toolbar>
                  <q-btn flat @click="drawer = !drawer" round dense icon="menu" />
                  <q-toolbar-title>User Sign in</q-toolbar-title>
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
                  <q-form ref="myForm" action="http://120.46.154.28:9999/user" method="post" @submit="submitNewAddress"
                    @reset="onReset" class="q-gutter-md">
                    <br />
                    <q-input></q-input>

                    <div class="submit">
                      <q-btn label="Submit" type="submit" color="primary" />
                      <q-btn label="Reset" type="reset" color="primary" flat class="q-ml-sm" />
                    </div>
                  </q-form>
                </q-page>
              </q-page-container>
            </q-layout>
          </q-dialog>
        </div>

      </div>

      <!-- 收货地址信息 -->
      <div v-if="showAddress" style="width: 100%; ">
        <h5
          style="line-height: 5px; font-weight: 500; font-size: 30px; font-family: system-ui, -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen, Ubuntu, Cantarell, 'Open Sans', 'Helvetica Neue', sans-serif;">
          我的收货地址
        </h5>
        <q-btn outline rounded color="deep-purple-6" label="添加收货地址" style="margin-top: 2%;"
          @click="add_Add_layout = true" />
        <div v-for="add in address_lst" :key="add" class="add-info" style="overflow: hidden; flex-grow: 9;">
          <q-card class="my-card" flat bordered style="width: 80%; max-width: 100%; margin-top: 3%;">
            <q-card-section style="overflow: hidden; width: 100%;">

              <div class="card-box" style="display: flex;">
                <div class="logo"
                  style="display: flex; flex-direction: column; justify-content: center; align-items: center;">
                  <div class="defaultLogo" v-if="add.isDefault" style="flex-grow: 1; display: flex; align-items: center;">
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

                <div class="add-operation" style="overflow: hidden; flex-grow: 1; display: flex;">
                  <q-btn class="edit-address" icon="edit" size="xs" style="flex-grow: 1;" flat
                    @click="editAddress(add)" />
                  <q-btn class="decrement" icon="delete" size="xs" style="flex-grow: 1;" flat
                    @click="confirmDeleteSingleAddress(add)" />
                </div>
              </div>

            </q-card-section>
          </q-card>

          <q-dialog v-model="edit_add_layout" no-click-outside-close>
            <q-layout view="Lhh lpR fff" container class="bg-white">
              <q-header class="bg-primary">
                <q-toolbar>
                  <q-btn flat @click="drawer = !drawer" round dense icon="menu" />
                  <q-toolbar-title>修改收货地址</q-toolbar-title>
                  <q-btn flat @click="drawerR = !drawerR;" round dense icon="menu" />
                  <q-btn flat v-close-popup round dense icon="close" @click="edit_add_layout = false" />
                </q-toolbar>
              </q-header>

              <q-page-container>
                <q-page padding>
                  <q-form ref="myForm" @submit="submitEditAddress" @reset="onReset" class="q-gutter-md">
                    <br>
                    <q-input filled v-model="edit_recipiant" hint="Recipient Name" lazy-rules :rules="[val => val && val.length > 0 && val.length < 13 || '请输入收货人姓名',
                      // val => code != 20009 || '店铺名已被注册过'
                    ]" />

                    <q-input filled v-model="edit_rec_tele" hint="Recipient's phone number" lazy-rules :rules="[
                      (val) => (val && val.length > 0) || '请输入收货人手机号',
                      (val) => val.length === 11 || '请输入正确的手机号',
                    ]" @click="code = 0" />

                    <q-input filled v-model="edit_address" hint="Recipient's address" lazy-rules
                      :rules="[val => val && val.length > 0 || '请输入收货地址']"></q-input>

                    <q-toggle v-model="edit_set_this_add_default" checked-icon="check" color="red" label="设置为默认地址"
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
      </div>

      <!-- 订单管理 -->
      <div v-if="showOrder" style="width: 100%; ">
        <h5
          style="line-height: 5px; font-weight: 500; font-size: 30px; font-family: system-ui, -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen, Ubuntu, Cantarell, 'Open Sans', 'Helvetica Neue', sans-serif;">
          我的订单
        </h5>

        <div class="btn-box" style="display: flex;">
          <q-btn flat color="secondary" label="待支付" style="flex-grow: 1;" @click="showNotPaidOrder" />
          <q-btn flat color="secondary" label="待发货" style="flex-grow: 1;" @click="showNotDelivered" />
          <q-btn flat color="secondary" label="待收货" style="flex-grow: 1;" @click="showDelivering" />
          <q-btn flat color="secondary" label="已完成" style="flex-grow: 1;" @click="showCompleted" />
          <q-btn flat color="secondary" label="退款中" style="flex-grow: 1;" @click="showRefundingOrder" />
          <q-btn flat color="secondary" label="已退款" style="flex-grow: 1;" @click="showRefundedOrder" />
          <q-btn flat color="secondary" label="已撤销" style="flex-grow: 1;" @click="showCancelOrder" />
        </div>

        <div v-if="not_paid_order" class="user-order-notPaid">
          <div v-if="not_paid_order_loading">
            <q-spinner-hourglass style="margin:auto" color="purple" size="4em" />
          </div>
          <div v-else>
            <div v-if="unpaid_order_lst.length === 0">
              <div class="text-caption text-grey" style="text-align: center; margin-top: 10px;">暂时没有待支付订单哦，快去下单吧！</div>
            </div>

            <div v-else>
              <div v-for="unpaid_order in unpaid_order_lst" :key="unpaid_order">
                <q-card class="shop-card" flat bordered style="width: 95%; margin-top: 2%; display: flex;">
                  <q-card-section horizontal style="flex-grow: 9.5; width: 85%;">
                    <q-card-section class="q-pt-xs" style="width: 100%; display: flex;">

                      <div class="customer-order-text" style="width: 100%; flex-grow: 5;">
                        <div class="text-overline text-grey-10">订单编号{{ unpaid_order.userOrder.id }}</div>

                        <div class="q-mt-sm q-mb-xs" style="font-size: large;">商店名称{{
                          unpaid_order.shop.shopName }}</div>
                        <div class="text-bold q-mt-sm q-mb-xs" style="font-size: large;">商品名称{{
                          unpaid_order.goods.goodsName }}</div>
                        <div class="text-bold q-mt-sm q-mb-xs" style="margin-left: 3px; color: gray;">×
                          数量{{ unpaid_order.userOrder.quantity }}</div>
                        <div class="text-bold q-mt-sm q-mb-xs">实付款：¥{{ unpaid_order.userOrder.totalPrice }}</div>

                        <div class="text q-mt-sm q-mb-xs">收货人：{{ unpaid_order.deliveryAddress.name }}&nbsp; &nbsp; 联系电话：{{
                          unpaid_order.deliveryAddress.phoneNumber }}</div>
                        <div class="text q-mt-sm q-mb-xs">收货地址：{{ unpaid_order.deliveryAddress.address }}</div>

                      </div>

                      <div class="customer-order-pict" style=" flex-grow: 5;">
                        <!-- TODO: 填上商品图片 -->
                        <q-img class="rounded-borders" :src="unpaid_order.goods.image[0]"
                          style="width:200px; height:150px"></q-img>
                      </div>

                    </q-card-section>
                  </q-card-section>

                  <q-card-actions style="flex-grow: 0.5;">
                    <!-- TODO: 补充操作 -->
                    <div class="btn-div"
                      style=" display: flex; flex-direction: column; align-items: center; justify-content: flex-start; height: 80%;">
                      <q-btn flat style="color: #570089; font-size: large; flex-grow: 1;" label="支付"
                        @click="confirmPayOrder(unpaid_order)" />
                      <q-btn flat style="color: #570089; font-size: large; flex-grow: 1;" label="取消订单"
                        @click="confirmCancelOrder(unpaid_order)" />
                    </div>
                  </q-card-actions>
                </q-card>
              </div>

              <div class="btn-box" style="display: flex;">
                <q-btn flat color="secondary" label="上一页" style="flex-grow: 1;"
                  @click="unpaid_current_page += 1; getUnpaidOrder(unpaid_current_page)"
                  :disabled="unpaid_current_page === 1" />
                <q-btn flat color="secondary" label="下一页" style="flex-grow: 1;"
                  @click="unpaid_current_page += 1; getUnpaidOrder(unpaid_current_page)"
                  :disabled="unpaid_current_page === unpaid_total_page" />
              </div>
            </div>
          </div>
        </div>

        <div v-if="not_delivered_order" class="user-order-notDelivered">
          <div v-if="not_delivered_order_loading">
            <q-spinner-hourglass style="margin:auto" color="purple" size="4em" />
          </div>

          <div v-else>
            <div v-if="not_delivered_order_lst.length === 0">
              <div class="text-caption text-grey" style="text-align: center; margin-top: 10px;">暂时没有未发货订单哦，快去下单吧！</div>
            </div>

            <div v-else>
              <div v-for="not_delivered_order in not_delivered_order_lst" :key="not_delivered_order">
                <q-card class="shop-card" flat bordered style="width: 95%; margin-top: 2%; display: flex;">
                  <q-card-section horizontal style="flex-grow: 9.5; width: 85%;">
                    <q-card-section class="q-pt-xs" style="width: 100%; display: flex;">

                      <div class="customer-order-text" style="width: 100%; flex-grow: 5;">
                        <div class="text-overline text-grey-10">订单编号{{ not_delivered_order.userOrder.id }}</div>

                        <div class="q-mt-sm q-mb-xs" style="font-size: large;">商店名称{{
                          not_delivered_order.shop.shopName }}</div>
                        <div class="text-bold q-mt-sm q-mb-xs" style="font-size: large;">商品名称{{
                          not_delivered_order.goods.goodsName }}</div>
                        <div class="text-bold q-mt-sm q-mb-xs" style="margin-left: 3px; color: gray;">×
                          数量{{ not_delivered_order.userOrder.quantity }}</div>
                        <div class="text-bold q-mt-sm q-mb-xs">实付款：¥{{ not_delivered_order.userOrder.totalPrice }}</div>

                        <div class="text q-mt-sm q-mb-xs">收货人：{{ not_delivered_order.deliveryAddress.name }}&nbsp; &nbsp;
                          联系电话：{{
                            not_delivered_order.deliveryAddress.phoneNumber }}</div>
                        <div class="text q-mt-sm q-mb-xs">收货地址：{{ not_delivered_order.deliveryAddress.address }}</div>

                      </div>

                      <div class="customer-order-pict" style=" flex-grow: 5;">

                        <q-img class="rounded-borders" :src="not_delivered_order.goods.image[0]"
                          style="width:200px; height:150px"></q-img>
                      </div>

                    </q-card-section>
                  </q-card-section>

                  <q-card-actions style="flex-grow: 0.5;">

                    <q-btn flat style="color: #570089; font-size: large;" label="退货退款"
                      @click="confirmRefundOrder(not_delivered_order)" />
                  </q-card-actions>
                </q-card>
              </div>

              <div class="btn-box" style="display: flex;">
                <q-btn flat color="secondary" label="上一页" style="flex-grow: 1;"
                  @click="not_delivered_current_page -= 1; getNotdeliveredOrder(not_delivered_current_page)"
                  :disabled="not_delivered_current_page === 1" />
                <q-btn flat color="secondary" label="下一页" style="flex-grow: 1;"
                  @click="not_delivered_current_page += 1; getNotdeliveredOrder(not_delivered_current_page)"
                  :disabled="not_delivered_current_page === not_delivered_total_page" />
              </div>
            </div>
          </div>
        </div>

        <div v-if="delivering_order" class="user-order-delivering">
          <div v-if="delivering_order_loading">
            <q-spinner-hourglass style="margin:auto" color="purple" size="4em" />
          </div>
          <div v-else>
            <div v-if="delivering_order_lst.length === 0">
              <div class="text-caption text-grey" style="text-align: center; margin-top: 10px;">暂时没有运输中的订单哦，快去下单吧！</div>
            </div>

            <div v-else>
              <div v-for="delivering_order in delivering_order_lst" :key="delivering_order">
                <q-card class="shop-card" flat bordered style="width: 95%; margin-top: 2%; display: flex;">
                  <q-card-section class="q-pt-xs" style="width: 100%; display: flex;">
                    <div class="customer-order-text" style="width: 100%; flex-grow: 5;">
                      <div class="text-overline text-grey-10">订单编号{{ delivering_order.userOrder.id }}</div>

                      <div class="q-mt-sm q-mb-xs" style="font-size: large;">商店名称{{
                        delivering_order.shop.shopName }}</div>
                      <div class="text-bold q-mt-sm q-mb-xs" style="font-size: large;">商品名称{{
                        delivering_order.goods.goodsName }}</div>
                      <div class="text-bold q-mt-sm q-mb-xs" style="margin-left: 3px; color: gray;">×
                        数量{{ delivering_order.userOrder.quantity }}</div>
                      <div class="text-bold q-mt-sm q-mb-xs">实付款：¥{{ delivering_order.userOrder.totalPrice }}</div>

                      <div class="text q-mt-sm q-mb-xs">收货人：{{ delivering_order.deliveryAddress.name }}&nbsp; &nbsp;
                        联系电话：{{
                          delivering_order.deliveryAddress.phoneNumber }}</div>
                      <div class="text q-mt-sm q-mb-xs">收货地址：{{ delivering_order.deliveryAddress.address }}</div>

                    </div>

                    <div class="customer-order-pict" style=" flex-grow: 5;">
                      <!-- TODO: 填上商品图片 -->
                      <q-img class="rounded-borders" :src="delivering_order.goods.image[0]"
                        style="width:200px; height:150px"></q-img>
                    </div>
                  </q-card-section>

                  <q-card-actions style="flex-grow: 0.5;">
                    <!-- TODO: 补充操作 -->
                    <div class="btn-div"
                      style=" display: flex; flex-direction: column; align-items: center; justify-content: flex-start; height: 80%;">
                      <q-btn flat style="color: #570089; font-size: large; flex-grow: 1;" label="确认收货"
                        @click="ensureReceiveOrder(delivering_order)" />
                      <q-btn flat style="color: #570089; font-size: large; flex-grow: 1;" label="退货退款"
                        @click="confirmRefundOrder(delivering_order)" />
                    </div>
                  </q-card-actions>
                </q-card>
              </div>

              <div class="btn-box" style="display: flex;">
                <q-btn flat color="secondary" label="上一页" style="flex-grow: 1;"
                  @click="delivering_current_page -= 1; getDeliveringOrder(delivering_current_page)"
                  :disabled="delivering_current_page === 1" />
                <q-btn flat color="secondary" label="下一页" style="flex-grow: 1;"
                  @click="delivering_current_page += 1; getDeliveringOrder(delivering_current_page)"
                  :disabled="delivering_current_page === delivering_total_page" />
              </div>
            </div>
          </div>
        </div>

        <div v-if="completed_order" class="user-order-completed">
          <div v-if="finished_order_loading">
            <q-spinner-hourglass style="margin:auto" color="purple" size="4em" />
          </div>
          <div v-else>
            <div v-if="finished_order_lst.length === 0">
              <div class="text-caption text-grey" style="text-align: center; margin-top: 10px;">暂时没有已完成的订单哦，快去下单吧！</div>
            </div>

            <div v-else>
              <div v-for="finished_order in finished_order_lst" :key="finished_order">
                <q-card class="shop-card" flat bordered style="width: 95%; margin-top: 2%; display: flex;">
                  <q-card-section class="q-pt-xs" style="width: 100%; display: flex;">
                    <div class="customer-order-text" style="width: 100%; flex-grow: 5;">
                      <div class="text-overline text-grey-10">订单编号{{ finished_order.userOrder.id }}</div>

                      <div class="q-mt-sm q-mb-xs" style="font-size: large;">商店名称{{
                        finished_order.shop.shopName }}</div>
                      <div class="text-bold q-mt-sm q-mb-xs" style="font-size: large;">商品名称{{
                        finished_order.goods.goodsName }}</div>
                      <div class="text-bold q-mt-sm q-mb-xs" style="margin-left: 3px; color: gray;">×
                        数量{{ finished_order.userOrder.quantity }}</div>
                      <div class="text-bold q-mt-sm q-mb-xs">实付款：¥{{ finished_order.userOrder.totalPrice }}</div>

                      <div class="text q-mt-sm q-mb-xs">收货人：{{ finished_order.deliveryAddress.name }}&nbsp; &nbsp; 联系电话：{{
                        finished_order.deliveryAddress.phoneNumber }}</div>
                      <div class="text q-mt-sm q-mb-xs">收货地址：{{ finished_order.deliveryAddress.address }}</div>

                    </div>

                    <div class="customer-order-pict" style=" flex-grow: 5;">
                      <!-- TODO: 填上商品图片 -->
                      <q-img class="rounded-borders" :src="finished_order.goods.image[0]"
                        style="width:200px; height:150px"></q-img>
                    </div>
                  </q-card-section>

                  <q-card-actions style="flex-grow: 0.5; display: flex; ">
                    <!-- TODO: 补充操作 -->

                    <q-btn flat style="color: #570089; font-size: large; flex-grow: 1;" label="删除订单"
                      @click="confirmDeleteOrder(finished_order)" />
                  </q-card-actions>
                </q-card>
              </div>

              <div class="btn-box" style="display: flex;">
                <q-btn flat color="secondary" label="上一页" style="flex-grow: 1;"
                  @click="finished_current_page -= 1; getFinishedOrder(finished_current_page)"
                  :disabled="finished_current_page === 1" />
                <q-btn flat color="secondary" label="下一页" style="flex-grow: 1;"
                  @click="finished_current_page += 1; getFinishedOrder(finished_current_page)"
                  :disabled="finished_current_page === finished_total_page" />
              </div>
            </div>
          </div>
        </div>

        <div v-if="refunding_order" class="user-refund-order">
          <div v-if="refunding_order_loading">
            <q-spinner-hourglass style="margin:auto" color="purple" size="4em" />
          </div>

          <div v-else>
            <div v-if="refunding_order_lst.length === 0">
              <div class="text-caption text-grey" style="text-align: center; margin-top: 10px;">暂时没有退款中的订单哦</div>
            </div>

            <div v-else>
              <div v-for="refunding_order in refunding_order_lst" :key="refunding_order">
                <q-card class="shop-card" flat bordered style="width: 95%; margin-top: 2%; display: flex;">
                  <q-card-section class="q-pt-xs" style="width: 100%; display: flex;">
                    <div class="customer-order-text" style="width: 100%; flex-grow: 5;">
                      <div class="text-overline text-grey-10">订单编号{{ refunding_order.userOrder.id }}</div>

                      <div class="q-mt-sm q-mb-xs" style="font-size: large;">商店名称{{
                        refunding_order.shop.shopName }}</div>
                      <div class="text-bold q-mt-sm q-mb-xs" style="font-size: large;">商品名称{{
                        refunding_order.goods.goodsName }}</div>
                      <div class="text-bold q-mt-sm q-mb-xs" style="margin-left: 3px; color: gray;">×
                        数量{{ refunding_order.userOrder.quantity }}</div>
                      <div class="text-bold q-mt-sm q-mb-xs">实付款：¥{{ refunding_order.userOrder.totalPrice }}</div>

                      <div class="text q-mt-sm q-mb-xs">收货人：{{ refunding_order.deliveryAddress.name }}&nbsp; &nbsp;
                        联系电话：{{
                          refunding_order.deliveryAddress.phoneNumber }}</div>
                      <div class="text q-mt-sm q-mb-xs">收货地址：{{ refunding_order.deliveryAddress.address }}</div>

                    </div>

                    <div class="customer-order-pict" style=" flex-grow: 5;">
                      <!-- TODO: 填上商品图片 -->
                      <q-img class="rounded-borders" :src="refunding_order.goods.image[0]"
                        style="width:200px; height:150px"></q-img>
                    </div>
                  </q-card-section>
                </q-card>
              </div>

              <div class="btn-box" style="display: flex;">
                <q-btn flat color="secondary" label="上一页" style="flex-grow: 1;"
                  @click="refunding_current_page -= 1; getrefundingOrder(refunding_current_page)"
                  :disabled="refunding_current_page === 1" />
                <q-btn flat color="secondary" label="下一页" style="flex-grow: 1;"
                  @click="refunding_current_page += 1; getrefundingOrder(refunding_current_page)"
                  :disabled="refunding_current_page === refunding_total_page" />
              </div>
            </div>
          </div>
        </div>

        <div v-if="refunded_order" class="user-refund-order">
          <div v-if="refunded_order_loading">
            <q-spinner-hourglass style="margin:auto" color="purple" size="4em" />
          </div>
          <div v-else>
            <div v-if="refunded_order_lst.length === 0">
              <div class="text-caption text-grey" style="text-align: center; margin-top: 10px;">暂时没有已退款的订单哦</div>
            </div>

            <div v-else>
              <div v-for="refunded_order in refunded_order_lst" :key="refunded_order">
                <q-card class="shop-card" flat bordered style="width: 95%; margin-top: 2%; display: flex;">
                  <q-card-section class="q-pt-xs" style="width: 100%; display: flex;">
                    <div class="customer-order-text" style="width: 100%; flex-grow: 5;">
                      <div class="text-overline text-grey-10">订单编号{{ refunded_order.userOrder.id }}</div>

                      <div class="q-mt-sm q-mb-xs" style="font-size: large;">商店名称{{
                        refunded_order.shop.shopName }}</div>
                      <div class="text-bold q-mt-sm q-mb-xs" style="font-size: large;">商品名称{{
                        refunded_order.goods.goodsName }}</div>
                      <div class="text-bold q-mt-sm q-mb-xs" style="margin-left: 3px; color: gray;">×
                        数量{{ refunded_order.userOrder.quantity }}</div>
                      <div class="text-bold q-mt-sm q-mb-xs">实付款：¥{{ refunded_order.userOrder.totalPrice }}</div>

                      <div class="text q-mt-sm q-mb-xs">收货人：{{ refunded_order.deliveryAddress.name }}&nbsp; &nbsp; 联系电话：{{
                        refunded_order.deliveryAddress.phoneNumber }}</div>
                      <div class="text q-mt-sm q-mb-xs">收货地址：{{ refunded_order.deliveryAddress.address }}</div>

                    </div>

                    <div class="customer-order-pict" style=" flex-grow: 5;">
                      <!-- TODO: 填上商品图片 -->
                      <q-img class="rounded-borders" :src="refunded_order.goods.image[0]"
                        style="width:200px; height:150px"></q-img>
                    </div>
                  </q-card-section>

                  <q-card-actions style="flex-grow: 0.5;">
                    <q-btn flat style="color: #570089; font-size: large; flex-grow: 1;" label="删除订单"
                      @click="confirmDeleteOrder(refunded_order)" />
                  </q-card-actions>
                </q-card>
              </div>

              <div class="btn-box" style="display: flex;">
                <q-btn flat color="secondary" label="上一页" style="flex-grow: 1;"
                  @click="refunded_current_page -= 1; getRefundedOrder(refunded_current_page)"
                  :disabled="refunded_current_page === 1" />
                <q-btn flat color="secondary" label="下一页" style="flex-grow: 1;"
                  @click="refunded_current_page += 1; getRefundedOrder(refunded_current_page)"
                  :disabled="refunded_current_page === refunded_total_page" />
              </div>
            </div>
          </div>
        </div>

        <div v-if="cancel_order" class="user-canceled-order">
          <div v-if="cancel_order_loading">
            <q-spinner-hourglass style="margin:auto" color="purple" size="4em" />
          </div>
          <div v-else>
            <div v-if="cancelled_order_lst.length === 0">
              <div class="text-caption text-grey" style="text-align: center; margin-top: 10px;">暂时没有已取消的订单哦</div>
            </div>

            <div v-else>
              <div v-for="cancelled_order in cancelled_order_lst" :key="cancelled_order">
                <q-card class="shop-card" flat bordered style="width: 95%; margin-top: 2%; display: flex;">
                  <q-card-section class="q-pt-xs" style="width: 100%; display: flex;">
                    <div class="customer-order-text" style="width: 100%; flex-grow: 5;">
                      <div class="text-overline text-grey-10">订单编号{{ cancelled_order.userOrder.id }}</div>

                      <div class="q-mt-sm q-mb-xs" style="font-size: large;">商店名称{{
                        cancelled_order.shop.shopName }}</div>
                      <div class="text-bold q-mt-sm q-mb-xs" style="font-size: large;">商品名称{{
                        cancelled_order.goods.goodsName }}</div>
                      <div class="text-bold q-mt-sm q-mb-xs" style="margin-left: 3px; color: gray;">×
                        数量{{ cancelled_order.userOrder.quantity }}</div>
                      <div class="text-bold q-mt-sm q-mb-xs">实付款：¥{{ cancelled_order.userOrder.totalPrice }}</div>

                      <div class="text q-mt-sm q-mb-xs">收货人：{{ cancelled_order.deliveryAddress.name }}&nbsp; &nbsp;
                        联系电话：{{
                          cancelled_order.deliveryAddress.phoneNumber }}</div>
                      <div class="text q-mt-sm q-mb-xs">收货地址：{{ cancelled_order.deliveryAddress.address }}</div>

                    </div>

                    <div class="customer-order-pict" style=" flex-grow: 5;">
                      <!-- TODO: 填上商品图片 -->
                      <q-img class="rounded-borders" :src="cancelled_order.goods.image[0]"
                        style="width:200px; height:150px"></q-img>
                    </div>
                  </q-card-section>

                  <q-card-actions style="flex-grow: 0.5;">
                    <q-btn flat style="color: #570089; font-size: large; flex-grow: 1;" label="删除订单"
                      @click="confirmDeleteOrder(cancelled_order)" />
                  </q-card-actions>
                </q-card>
              </div>
              <div class="btn-box" style="display: flex;">
                <q-btn flat color="secondary" label="上一页" style="flex-grow: 1;"
                  @click="cancelled_current_page -= 1; getCancelledOrder(cancelled_current_page)"
                  :disabled="cancelled_current_page === 1" />
                <q-btn flat color="secondary" label="下一页" style="flex-grow: 1;"
                  @click="cancelled_current_page += 1; getCancelledOrder(cancelled_current_page)"
                  :disabled="cancelled_current_page === cancelled_total_page" />
              </div>
            </div>
          </div>
        </div>

      </div>


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

      <q-dialog v-model="edit_add_layout" no-click-outside-close>
        <q-layout view="Lhh lpR fff" container class="bg-white">
          <q-header class="bg-primary">
            <q-toolbar>
              <q-btn flat @click="drawer = !drawer" round dense icon="menu" />
              <q-toolbar-title>修改收货地址</q-toolbar-title>
              <q-btn flat @click="drawerR = !drawerR;" round dense icon="menu" />
              <q-btn flat v-close-popup round dense icon="close" @click="edit_add_layout = false" />
            </q-toolbar>
          </q-header>

          <q-page-container>
            <q-page padding>
              <q-form ref="myForm" @submit="submitEditAddress" @reset="onReset" class="q-gutter-md">
                <br>
                <q-input filled v-model="edit_recipiant" hint="Recipient Name" lazy-rules :rules="[val => val && val.length > 0 && val.length < 13 || '请输入收货人姓名',
                  // val => code != 20009 || '店铺名已被注册过'
                ]" />

                <q-input filled v-model="edit_rec_tele" hint="Recipient's phone number" lazy-rules :rules="[
                  (val) => (val && val.length > 0) || '请输入收货人手机号',
                  (val) => val.length === 11 || '请输入正确的手机号',
                ]" @click="code = 0" />

                <q-input filled v-model="edit_address" hint="Recipient's address" lazy-rules
                  :rules="[val => val && val.length > 0 || '请输入收货地址']"></q-input>

                <q-toggle v-model="edit_set_this_add_default" checked-icon="check" color="red" label="设置为默认地址"
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

      <q-dialog v-model="confirm_delete_address" no-click-outside-close>
        <q-layout view="Lhh lpR fff" container class="bg-white">
          <q-header class="bg-primary">
            <q-toolbar>
              <q-btn flat @click="drawer = !drawer" round dense icon="menu" />
              <q-toolbar-title>删除收货地址确认</q-toolbar-title>
              <q-btn flat @click="drawerR = !drawerR;" round dense icon="menu" />
              <q-btn flat v-close-popup round dense icon="close" @click="confirm_delete_address = false" />
            </q-toolbar>
          </q-header>

          <q-page-container>
            <q-page padding>
              <q-card-section class="q-pt-none text-h6" style="margin-top: 5%;">
                确认要删除收货地址吗？
              </q-card-section>
              <q-form ref="myForm" @submit="deleteSingleAddress" @reset="onReset" class="q-gutter-md">
                <br>
                <q-input filled v-model="delete_recipiant" hint="Recipient Name" disable />

                <q-input filled v-model="delete_rec_tele" hint="Recipient's phone number" disable />

                <q-input filled v-model="delete_address" hint="Recipient's address" disable />

                <q-toggle v-model="delete_is_default" checked-icon="check" color="red" label="设置为默认地址"
                  unchecked-icon="clear" disable />

                <div class="submit">
                  <q-btn label="确认删除" type="submit" color="negative" flat class="q-ml-sm" style="font-size: large;"
                    @click="deleteSingleAddress(delete_address_id)" />
                  <q-btn label="取消" type="reset" color="indigo-10" flat class="q-ml-sm" style="font-size: large;"
                    @click="confirm_delete_address = false" />
                </div>
              </q-form>
            </q-page>
          </q-page-container>
        </q-layout>
      </q-dialog>

      <q-dialog v-model="pay_order_layout" no-click-outside-close>
        <q-layout view="Lhh lpR fff" container class="bg-white">
          <q-header class="bg-primary">
            <q-toolbar>
              <q-btn flat @click="drawer = !drawer" round dense icon="menu" />
              <q-toolbar-title>支付订单确认</q-toolbar-title>
              <q-btn flat @click="drawerR = !drawerR;" round dense icon="menu" />
              <q-btn flat v-close-popup round dense icon="close" @click="delete_order_layout = false" />
            </q-toolbar>
          </q-header>

          <q-page-container>
            <q-page padding>
              <q-card class="shop-card" flat bordered style="width: 95%; margin-top: 2%; ">
                <q-card-section class="q-pt-xs" style="width: 100%; display: flex;">
                  <div class="customer-order-text" style="width: 100%; flex-grow: 5;">
                    <div class="text-overline text-grey-10">订单编号{{ pay_order.userOrder.id }}</div>

                    <div class="q-mt-sm q-mb-xs" style="font-size: large;">商店名称{{
                      pay_order.shop.shopName }}</div>
                    <div class="text-bold q-mt-sm q-mb-xs" style="font-size: large;">商品名称{{
                      pay_order.goods.goodsName }}</div>
                    <div class="text-bold q-mt-sm q-mb-xs" style="margin-left: 3px; color: gray;">×
                      {{ cancel_order_quantity }}</div>
                    <div class="text-bold q-mt-sm q-mb-xs">实付款：¥{{ pay_order.userOrder.totalPrice }}</div>

                    <div class="text q-mt-sm q-mb-xs">收货人：{{ pay_order.deliveryAddress.name }}&nbsp; &nbsp;
                      联系电话：{{ pay_order.deliveryAddress.phoneNumber }}</div>
                    <div class="text q-mt-sm q-mb-xs">收货地址：{{ pay_order.deliveryAddress.address }}</div>

                  </div>

                  <div class="customer-order-pict" style=" flex-grow: 5;">
                    <!-- TODO: 填上商品图片 -->
                    <q-img class="rounded-borders" :src="pay_order.goods.image[0]"
                      style="width:200px; height:150px"></q-img>
                  </div>
                </q-card-section>

                <q-card-actions style="overflow: hidden; flex-grow: 1; display: flex;">
                  <div class="submit">
                    <q-btn label="确认支付" type="submit" color="negative" flat class="q-ml-sm" style="font-size: large;"
                      @click="payOrder(pay_order)" />
                    <q-btn label="取消" type="reset" color="indigo-10" flat class="q-ml-sm" style="font-size: large;"
                      @click="pay_order_layout = false" />
                  </div>
                </q-card-actions>
              </q-card>
            </q-page>
          </q-page-container>
        </q-layout>
      </q-dialog>

      <q-dialog v-model="delete_order_layout" no-click-outside-close>
        <q-layout view="Lhh lpR fff" container class="bg-white">
          <q-header class="bg-primary">
            <q-toolbar>
              <q-btn flat @click="drawer = !drawer" round dense icon="menu" />
              <q-toolbar-title>删除订单确认</q-toolbar-title>
              <q-btn flat @click="drawerR = !drawerR;" round dense icon="menu" />
              <q-btn flat v-close-popup round dense icon="close" @click="delete_order_layout = false" />
            </q-toolbar>
          </q-header>

          <q-page-container>
            <q-page padding>
              <q-card class="shop-card" flat bordered style="width: 95%; margin-top: 2%; ">
                <q-card-section class="q-pt-xs" style="width: 100%; display: flex;">
                  <div class="customer-order-text" style="width: 100%; flex-grow: 5;">
                    <div class="text-overline text-grey-10">订单编号{{ delete_order_id }}</div>

                    <div class="q-mt-sm q-mb-xs" style="font-size: large;">商店名称{{
                      delete_order_shopName }}</div>
                    <div class="text-bold q-mt-sm q-mb-xs" style="font-size: large;">商品名称{{
                      delete_order_goodsName }}</div>
                    <div class="text-bold q-mt-sm q-mb-xs" style="margin-left: 3px; color: gray;">×
                      {{ delete_order_quantity }}</div>
                    <div class="text-bold q-mt-sm q-mb-xs">实付款：¥{{ delete_order_totalPrice }}</div>

                    <div class="text q-mt-sm q-mb-xs">收货人：{{ delete_order_recipiant }}&nbsp; &nbsp;
                      联系电话：{{ delete_order_rec_tele }}</div>
                    <div class="text q-mt-sm q-mb-xs">收货地址：{{ delete_order_rec_address }}</div>

                  </div>

                  <div class="customer-order-pict" style=" flex-grow: 5;">
                    <!-- TODO: 填上商品图片 -->
                    <q-img class="rounded-borders" :src="delete_order_img[0]" style="width:200px; height:150px"></q-img>
                  </div>
                </q-card-section>

                <q-card-actions style="overflow: hidden; flex-grow: 1; display: flex;">
                  <div class="submit">
                    <q-btn label="确认删除" type="submit" color="negative" flat class="q-ml-sm" style="font-size: large;"
                      @click="deleteOrder(delete_order_id)" />
                    <q-btn label="取消" type="reset" color="indigo-10" flat class="q-ml-sm" style="font-size: large;"
                      @click="delete_order_layout = false" />
                  </div>
                </q-card-actions>
              </q-card>
            </q-page>
          </q-page-container>
        </q-layout>
      </q-dialog>

      <q-dialog v-model="cancel_order_layout" no-click-outside-close>
        <q-layout view="Lhh lpR fff" container class="bg-white">
          <q-header class="bg-primary">
            <q-toolbar>
              <q-btn flat @click="drawer = !drawer" round dense icon="menu" />
              <q-toolbar-title>取消订单确认</q-toolbar-title>
              <q-btn flat @click="drawerR = !drawerR;" round dense icon="menu" />
              <q-btn flat v-close-popup round dense icon="close" @click="delete_order_layout = false" />
            </q-toolbar>
          </q-header>

          <q-page-container>
            <q-page padding>
              <q-card class="shop-card" flat bordered style="width: 95%; margin-top: 2%; ">
                <q-card-section class="q-pt-xs" style="width: 100%; display: flex;">
                  <div class="customer-order-text" style="width: 100%; flex-grow: 5;">
                    <div class="text-overline text-grey-10">订单编号{{ cancel_order_id }}</div>

                    <div class="q-mt-sm q-mb-xs" style="font-size: large;">商店名称{{
                      cancel_order_shopName }}</div>
                    <div class="text-bold q-mt-sm q-mb-xs" style="font-size: large;">商品名称{{
                      cancel_order_goodsName }}</div>
                    <div class="text-bold q-mt-sm q-mb-xs" style="margin-left: 3px; color: gray;">×
                      {{ cancel_order_quantity }}</div>
                    <div class="text-bold q-mt-sm q-mb-xs">实付款：¥{{ cancel_order_totalPrice }}</div>

                    <div class="text q-mt-sm q-mb-xs">收货人：{{ cancel_order_recipiant }}&nbsp; &nbsp;
                      联系电话：{{ cancel_order_rec_tele }}</div>
                    <div class="text q-mt-sm q-mb-xs">收货地址：{{ cancel_order_rec_address }}</div>

                  </div>

                  <div class="customer-order-pict" style=" flex-grow: 5;">
                    <!-- TODO: 填上商品图片 -->
                    <q-img class="rounded-borders" :src="cancel_order_img[0]" style="width:200px; height:150px"></q-img>
                  </div>
                </q-card-section>

                <q-card-actions style="overflow: hidden; flex-grow: 1; display: flex;">
                  <div class="submit">
                    <q-btn label="确认取消订单" type="submit" color="negative" flat class="q-ml-sm" style="font-size: large;"
                      @click="cancelOrder(cancel_order_id)" />
                    <q-btn label="取消" type="reset" color="indigo-10" flat class="q-ml-sm" style="font-size: large;"
                      @click="cancel_order_layout = false" />
                  </div>
                </q-card-actions>
              </q-card>
            </q-page>
          </q-page-container>
        </q-layout>
      </q-dialog>

      <q-dialog v-model="refund_order_layout" no-click-outside-close>
        <q-layout view="Lhh lpR fff" container class="bg-white">
          <q-header class="bg-primary">
            <q-toolbar>
              <q-btn flat @click="drawer = !drawer" round dense icon="menu" />
              <q-toolbar-title>退货退款确认</q-toolbar-title>
              <q-btn flat @click="drawerR = !drawerR;" round dense icon="menu" />
              <q-btn flat v-close-popup round dense icon="close" @click="delete_order_layout = false" />
            </q-toolbar>
          </q-header>

          <q-page-container>
            <q-page padding>
              <q-card class="shop-card" flat bordered style="width: 95%; margin-top: 2%; ">
                <q-card-section class="q-pt-xs" style="width: 100%; display: flex;">
                  <div class="customer-order-text" style="width: 100%; flex-grow: 5;">
                    <div class="text-overline text-grey-10">订单编号{{ refund_order_id }}</div>

                    <div class="q-mt-sm q-mb-xs" style="font-size: large;">商店名称{{
                      refund_order_shopName }}</div>
                    <div class="text-bold q-mt-sm q-mb-xs" style="font-size: large;">商品名称{{
                      refund_order_goodsName }}</div>
                    <div class="text-bold q-mt-sm q-mb-xs" style="margin-left: 3px; color: gray;">×
                      {{ refund_order_quantity }}</div>
                    <div class="text-bold q-mt-sm q-mb-xs">实付款：¥{{ refund_order_totalPrice }}</div>

                    <div class="text q-mt-sm q-mb-xs">收货人：{{ refund_order_recipiant }}&nbsp; &nbsp;
                      联系电话：{{ refund_order_rec_tele }}</div>
                    <div class="text q-mt-sm q-mb-xs">收货地址：{{ refund_order_rec_address }}</div>

                  </div>

                  <div class="customer-order-pict" style=" flex-grow: 5;">
                    <!-- TODO: 填上商品图片 -->
                    <q-img class="rounded-borders" :src="refund_order_img[0]" style="width:200px; height:150px"></q-img>
                  </div>
                </q-card-section>

                <q-card-actions style="overflow: hidden; flex-grow: 1; display: flex;">
                  <div class="submit">
                    <q-btn label="确认申请退货退款" type="submit" color="negative" flat class="q-ml-sm" style="font-size: large;"
                      @click="refunding(refund_order_id)" />
                    <q-btn label="取消" type="reset" color="indigo-10" flat class="q-ml-sm" style="font-size: large;"
                      @click="refund_order_layout = false" />
                  </div>
                </q-card-actions>
              </q-card>
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
            <q-btn flat label="OK" color="secondary" v-close-popup @click="operation_success_alert_layout = false;" />
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
            <q-btn flat label="OK" color="secondary" v-close-popup @click="operation_fail_alert_layout = false;" />
          </q-card-actions>
        </q-card>
      </q-dialog>

      <q-page-scroller position="bottom-right" :scroll-offset="150" :offset="[18, 18]">
        <q-btn fab icon="keyboard_arrow_up" color="accent" />
      </q-page-scroller>

    </q-page-container>
  </q-layout>
</template>

<script setup>
import { ref, getCurrentInstance } from 'vue'
import { onMounted } from 'vue'
import { useQuasar } from 'quasar'
import { useStore } from 'src/store'
import PSL from 'psl';
import axios from 'axios'
import { useRouter } from 'vue-router'


const axiosInstance = axios.create({
  // baseURL: 'http://120.46.154.28:9999',
  baseURL: 'http://120.46.154.28:9999',
});
let response_register = ref(null)
let code = ref(null)

const leftDrawerOpen = ref(true)
const q = useQuasar()

// 功能变量
// url: swy GitHub的头像，以下网页链接是GitHub“储存”我的头像的链接
let Imgurl = ref('https://avatars.githubusercontent.com/u/105032850?s=400&u=285d7d130058e413bb8797cb52bc10f75c343076&v=4')

// 操作成功or失败的提示
let operation_success_alert_layout = ref(false)
let operation_success_message = ref(null)
let operation_fail_alert_layout = ref(false)
let operation_fail_message = ref(null)

// 对应渲染
let console_data = ref(null)
let console_order = ref(null)
let headerLabel = ref(null)

let showAccountInfo = ref(console_data.value === null && console_order.value === null)
let showAccountAmount = ref(false)
let showAddress = ref(console_data.value == null ? false : true)
let showOrder = ref(console_order.value == null ? false : true)

let isMerchant = ref(false)
let viewOnly = ref(true)        // 禁用/开放个人信息修改
let expanded = ref(false)   // 左侧栏开闭变量
let editNotification = ref(false)
let editMsg = ref(null)

// 收货地址相关的变量
let myForm = ref(null)
let address_lst = ref([])
let add_Add_layout = ref(false)     // 打开新增地址的dialogue
let set_this_add_default = ref(false)   // 设置默认地址
let pre_address = ref(null)
let pre_recipiant = ref(null)
let pre_rec_tele = ref(null)
let pre_is_default = ref(false)
let edit_address = ref(null)    // 打开修改地址的dialogue
let edit_rec_tele = ref(null)
let edit_recipiant = ref(null)
let edit_address_id = ref(null)
let edit_set_this_add_default = ref(false)
let confirm_delete_address = ref(false)

let delete_recipiant = ref(null)
let delete_rec_tele = ref(null)
let delete_address = ref(null)
let delete_address_id = ref(null)
let delete_is_default = ref(null)

// 用户展示各类订单状态的
let not_paid_order = ref(false)
let not_delivered_order = ref(false)
let delivering_order = ref(false)
let completed_order = ref(false)
let refunded_order = ref(false)
let refunding_order = ref(false)
let cancel_order = ref(false)

let userId = localStorage.getItem('userId')
let username = ref(null)
let pre_username = ref(null)
let phone = ref(null)
let pre_phone = ref(null)
let email = ref(null)
let pre_email = ref(null)
let password = ref(null)
let idNumber = ref(null)
let roleId = ref(null)
let money = ref(null)
let balance = ref(null)
let show_top_up = ref(false)    // 显示充值对话框

let recipient = ref(null)
let rec_address = ref(null)
let rec_tele = ref(null)
let edit_add_layout = ref(false)


let cancelled_total_page = ref(null)
let unpaid_total_page = ref(null)
let finished_total_page = ref(null)
let delivering_total_page = ref(null)
let not_delivered_total_page = ref(null)
let refunding_total_page = ref(null)
let refunded_total_page = ref(null)

let cancelled_current_page = 1
let unpaid_current_page = 1
let finished_current_page = 1
let delivering_current_page = 1
let not_delivered_current_page = 1
let refunding_current_page = 1
let refunded_current_page = 1

//加载
const refunding_order_loading = ref(false)
const refunded_order_loading = ref(false)
const cancel_order_loading = ref(false)
const delivering_order_loading = ref(false)
const finished_order_loading = ref(false)
const not_delivered_order_loading = ref(false)
const not_paid_order_loading = ref(false)

// 获取当前时间
let current_time = ref(null)
const instance = getCurrentInstance()

// 用户的订单分类
let unpaid_order_lst = ref([])  // 待付款
let not_delivered_order_lst = ref([])   // 待发货
let cancelled_order_lst = ref([])       // 已撤销
let refunded_order_lst = ref([])        // 已退款
let refunding_order_lst = ref([])       // 待退款
let delivering_order_lst = ref([])      // 待收货
let finished_order_lst = ref([])        // 已完成

let pay_order = ref(null)
let pay_order_layout = ref(false)

let delete_order_id = ref(null)
let delete_order_shopName = ref(null)
let delete_order_goodsName = ref(null)
let delete_order_quantity = ref(null)
let delete_order_totalPrice = ref(null)
let delete_order_recipiant = ref(null)
let delete_order_rec_tele = ref(null)
let delete_order_rec_address = ref(null)
let delete_order_img = ref(null)
let delete_order_layout = ref(false)

let cancel_order_id = ref(null)
let cancel_order_shopName = ref(null)
let cancel_order_goodsName = ref(null)
let cancel_order_quantity = ref(null)
let cancel_order_totalPrice = ref(null)
let cancel_order_recipiant = ref(null)
let cancel_order_rec_tele = ref(null)
let cancel_order_rec_address = ref(null)
let cancel_order_img = ref(null)
let cancel_order_layout = ref(false)

let refund_order_id = ref(null)
let refund_order_shopName = ref(null)
let refund_order_goodsName = ref(null)
let refund_order_quantity = ref(null)
let refund_order_totalPrice = ref(null)
let refund_order_recipiant = ref(null)
let refund_order_rec_tele = ref(null)
let refund_order_rec_address = ref(null)
let refund_order_img = ref(null)
let refund_order_layout = ref(false)


// 流水相关变量
let transitionCols = [
  { name: 'transferName', label: '转账人', field: 'transferName' },
  { name: 'receiveName', label: '收款人', field: 'receiveName' },
  { name: 'amount', label: '金额（¥）', field: 'amount' },
]
let transitionRows = ref([])
let transaction_range = ref('all_transactions') // 查看的流水范围

// 左侧边栏内项目icon-text列举
const links1 = [
  { icon: 'person', text: '个人账户' },
  { icon: 'star_border', text: '资金信息' },
  { icon: 'link', text: '收货地址' },
  { icon: 'shopping_cart', text: '我的订单' },
]

onMounted(() => {

  const router = useRouter();
  router.isReady().then(() => {
    console_data.value = router.currentRoute.value.query.id;
    if (console_data.value) {
      console.log('查询参数：id：' + console_data.value);
    } else {
      console_data.value = null
      console.log('id没有查询参数');
    }

    console_order.value = router.currentRoute.value.query.order;
    if (console_order.value) {
      console.log('查询参数：order：' + console_order.value);
    }
    else {
      console_order.value = null
      console.log('order没有查询参数');
    }

    showAccountInfo.value = console_data.value === null && console_order.value === null
    showAccountAmount.value = false
    showAddress.value = console_data.value == null ? false : true
    showOrder.value = console_order.value == null ? false : true

    instance.proxy.$forceUpdate();
  });

  // 获取用户信息
  console.log('userid: ' + userId)
  axiosInstance.post('/user/getUserInfo', {
    id: userId,
  }).then(response => {
    const r = response.data['data']
    console.log(r)
    username.value = r.username
    pre_username.value = r.username

    phone.value = r.phone
    pre_phone.value = r.phone

    email.value = r.email
    pre_email.value = r.email

    roleId.value = r.roleId
    password.value = r.password
    idNumber.value = r.idNumber

    if (roleId.value === 3) {
      isMerchant.value = true;
    } else {
      isMerchant.value = false;
    }
    console.log('Get user info\nusername: ' + username.value +
      '\nphone: ' + phone.value +
      '\nid number: ' + idNumber.value +
      '\nEmail: ' + email.value +
      '\nrole: ' + roleId.value +
      '\nisMerchant:' + isMerchant.value)
  });
  getAccount();
  console.log('balance: ', balance.value)
  getTransition();
  getAllAddresses();
  for (var i = 0; i < address_lst.value.length; i++) {
    add = address_lst.value[i]
    console.log('check delivery address: ', add.value)
  }
  if (isMerchant.value) {
    getShopRefundingOrderOrder()
  } else {
    getCancelledOrder()
    getDeliveringOrder()
    getFinishedOrder()
    getNotdeliveredOrder()
    getRefundedOrder()
    getRefundingOrder()
    getUnpaidOrder()
  }
});

// 深色模式
function toggle() {
  q.dark.toggle();
}

// 暂禁用
// 左侧边栏开闭
function toggleLeftDrawer() {
  leftDrawerOpen.value = !leftDrawerOpen.value
}

// 侧边栏对应渲染：分别设置布尔值 分情况讨论
function handleClick(link) {
  console.log('handle click: ', link)
  console.log("is merchant?: ", isMerchant.value)
  if (link.icon === 'person') {
    showAccountInfo.value = true
    showAccountAmount.value = false
    showAddress.value = false
    showOrder.value = false
    headerLabel.value = '个人信息'
  } else if (link.icon === 'star_border') {
    showAccountInfo.value = false
    showAccountAmount.value = true
    showAddress.value = false
    showOrder.value = false
    headerLabel.value = '我的账户'
  }
  else if (link.icon === 'link') {
    showAccountInfo.value = false
    showAccountAmount.value = false
    showAddress.value = true
    showOrder.value = false
    headerLabel.value = '我的收货地址'
  }
  else if (link.icon === 'shopping_cart') {
    showAccountInfo.value = false
    showAccountAmount.value = false
    showAddress.value = false
    showOrder.value = true
    headerLabel.value = '我的订单'
  }
  else {
    showAccountInfo.value = false
    showAccountAmount.value = false
    showAddress.value = false
    showOrder.value = false
    headerLabel.value = '个人信息'
  }
  console.log("after click: ",
    "show user info: ", showAccountInfo.value,
    "\nshow order: ", showOrder.value,
    "\nshow address: ", showAddress.value,
    "\nshow account: ", showAccountAmount.value)
}

function close_alert(alert_flag) {
  alert_flag = !alert_flag
}

function viewEditModeChange() {
  viewOnly.value = false
  console.log(viewOnly)
  editNotification.value = false
}

// 获取当前日期 YYYY-MM-DD
function getFormattedDate() {
  const now = new Date();
  const isoDate = now.toISOString();
  return isoDate.substr(0, 10);
}

function showTopUpDialogue() {
  show_top_up.value = true
}

function validateEmailDomain(val) {
  // 使用PSL方法检查邮箱域名是否合法，需要安装PSL包
  const domain = val.split('@')[1]
  const parsed = PSL.parse(domain)
  return !!parsed && !!parsed.domain
}

// 个人信息编辑的表单提交（总体设置成一个表单，随时修改）
function postProfileUpdate() {
  if (username.value === pre_username.value && phone.value === pre_phone.value && email.value === pre_email.value) {
    editNotification.value = true
    operation_fail_alert_layout.value = true
    operation_fail_message.value = '您未做任何修改！'
  } else {
    axiosInstance.put('/user/update', {
      id: userId,
      username: username.value,
      password: password.value,
      idNumber: idNumber.value,
      email: email.value,
      phone: phone.value,
      roleId: roleId.value,
    }).then((response) => {
      console.log('Response data:', response.data);
      response_register.value = response.data;
      console.log(response_register);
      code.value = response_register.value['code'];
      editNotification.value = true
      editMsg.value = response_register.value['message']
      if (code.value === 20000) {
        viewOnly.value = 1
      }
      console.log('Push success. ', 'Modify success:', viewOnly.value);
      console.log('Edit profile\nusername:', username.value, '\nphone:', phone.value, '\nemail:', email.value)
      console.log('Edit notification: ', editNotification.value, 'Edit message: ', editMsg.value)
    }).catch((error) => {
      console.error('Error:', error);
      // 处理错误
      editNotification.value = true
      editMsg.value = response_register.value['message']
      console.log('(error) Edit notification: ', editNotification.value, 'Edit message: ', editMsg.value)
    });
  }
}

function getAccount() {
  axiosInstance.post('user/getAccount', {
    id: userId,
  }).then((response) => {
    response_register.value = response['data']
    console.log('get account: ', response_register.value)
    balance.value = response_register.value['data']
    console.log('balance: ', balance.value)
    code.value = response_register.value['code']
  }).catch((error) => {
    console.error('Error: ', error);
  })
}

// 充值操作 触发：Enter
function topUpAccount() {
  console.log('top up money is: ', money.value)
  console.log('userid is: ', userId)
  axiosInstance.put(`/user/topUpAccount?userId=${userId}&money=${money.value}`
  ).then((response) => {
    console.log('Response data:', response.data);
    response_register.value = response.data;
    console.log("充值返回信息：", response_register);
    code.value = response_register.value['code'];
    balance.value = response_register.value['data'];
    if (code.value == 20000) {
      operation_success_message.value = '充值成功'
      operation_success_alert_layout.value = true
      show_top_up.value = false
      money.value = null
    } else {
      operation_fail_message.value = response_register.value['message']
      operation_fail_alert_layout.value = true
      money.value = null
    }
  }).catch((error) => {
    console.error('充值 Error:', error);

  });
}

// 获取个人账户的流水
function getTransition() {
  transitionRows.value = []
  console.log('current userid(transition): ', userId)
  if (transaction_range.value === 'all_transactions') {
    axiosInstance.post('/transferRecords/user', {
      id: userId
    }, {
      params: {
        id: userId
      }
    }).then((response) => {
      let r = response.data[1]['data']
      console.log('get user transfer records: ', r);
      if (r.length) {
        r.forEach(function (item) {
          if (item !== null) {
            const [year, month, day] = item.time;
            item.time = `${year}-${month}-${day}`;
            console.log(item);
            transitionRows.value.push(item);
          }
        });
      }
    });
    console.log('all transition: ', transitionRows.value)
  } else if (transaction_range.value === 'last_month_transactions') {
    axiosInstance.post('/transferRecords/userWithinOneMonth', {
      id: userId
    }, {
      params: {
        id: userId
      }
    }).then((response) => {
      let r = response.data[1]['data']
      console.log('get user transfer records: ', r);
      if (r.length) {
        r.forEach(function (item) {
          if (item !== null) {
            const [year, month, day] = item.time;
            item.time = `${year}-${month}-${day}`;
            console.log(item);
            transitionRows.value.push(item);
          }
        });
      }
    });
    console.log('last months transition: ', transitionRows.value)
  } else if (transaction_range.value === 'last_week_transactions') {
    axiosInstance.post('/transferRecords/userWithinOneWeek', {
      id: userId
    }, {
      params: {
        id: userId
      }
    }).then((response) => {
      let r = response.data[1]['data']
      console.log('get user transfer records: ', r);
      if (r.length) {
        r.forEach(function (item) {
          if (item !== null) {
            const [year, month, day] = item.time;
            item.time = `${year}-${month}-${day}`;
            console.log(item);
            transitionRows.value.push(item);
          }
        });
      }
    });
    console.log('last weeks transition: ', transitionRows.value)
  }
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
    editMsg.value = response_register.value['message']
    if (code.value === 20000 && editMsg.value === '添加成功') {
      set_this_add_default.value = false
      recipient.value = null
      rec_address.value = null
      rec_tele.value = null
      set_this_add_default.value = false
      operation_success_alert_layout.value = true
      operation_success_message.value = '新增收货地址成功'
      add_Add_layout.value = false
      getAllAddresses()
    }
  }).catch((error) => {
    console.error('Error:', error);
  });
}

// 提交修改地址表单
function submitEditAddress() {
  console.log("修改收货地址时获取user id: ", userId)
  console.log("修改收货地址 之前的地址：\n收货人：", pre_recipiant.value, "\n联系方式：", pre_rec_tele.value,
    "\n收货地址", pre_address.value, "\nis default: ", pre_is_default.value)
  if (edit_address.value !== pre_address.value
    || edit_recipiant.value !== pre_recipiant.value
    || edit_rec_tele.value !== pre_rec_tele.value
    || edit_set_this_add_default.value !== pre_is_default.value) {
    console.log("修改收货地址提交：\n收货人：", edit_recipiant.value, "\n联系方式：", edit_rec_tele.value,
      "\n收货地址", edit_address.value, "\nis default: ", edit_set_this_add_default.value)
    axiosInstance.put('/user/updateDeliveryAddress', {
      userId: userId,
      id: edit_address_id.value,
      name: edit_recipiant.value,
      address: edit_address.value,
      phoneNumber: edit_rec_tele.value,
      isDefault: Boolean(edit_set_this_add_default.value),
    }).then((response) => {
      console.log('Response data:', response.data);
      response_register.value = response.data;

      code.value = response_register.value['code'];
      editMsg.value = response_register.value['message']
      console.log("修改地址返回信息：", code.value, '\t', editMsg.value);
      if (code.value === 20000 && editMsg.value === '修改成功') {
        edit_address.value = null
        edit_recipiant.value = null
        edit_rec_tele.value = null
        operation_success_alert_layout.value = true
        operation_success_message.value = '收货地址修改成功'
        edit_add_layout.value = false
        getAllAddresses()
      } else {
        operation_fail_message.value = editMsg.value
        operation_fail_alert_layout.value = true
        edit_add_layout.value = false
      }
    }).catch((error) => {
      console.error('Error:', error);
      // 处理错误
    });
  } else {
    operation_fail_message.value = '您没有做任何修改。'
    operation_fail_alert_layout.value = true
  }
  set_this_add_default.value = false
  edit_add_layout.value = false
}

function confirmDeleteSingleAddress(address) {
  confirm_delete_address.value = true
  console.log("确认删除地址: ", address)
  delete_recipiant.value = address.name
  delete_rec_tele.value = address.phoneNumber
  delete_address.value = address.address
  delete_address_id.value = address.id
  delete_is_default.value = address.isDefault
}

function deleteSingleAddress(delete_address_id) {
  console.log("delete address id: ", delete_address_id)
  axiosInstance.delete('/user/deleteDeliveryAddress', {
    params: {
      id: delete_address_id
    }
  }).then((res) => {
    console.log("delete address response: ", res.data)
    if (res.data['code'] === 20000 && res.data['message'] === "删除成功") {
      operation_success_alert_layout.value = true
      operation_success_message.value = '已删除收货地址'
      confirm_delete_address.value = false
      getAllAddresses()
    } else {
      operation_fail_message.value = res.data['message']
      operation_fail_alert_layout.value = true
      confirm_delete_address.value = false
    }
  });
}

function editAddress(address) {
  edit_add_layout.value = true
  pre_address.value = address.address
  pre_recipiant.value = address.name
  pre_rec_tele.value = address.phoneNumber
  pre_is_default.value = address.isDefault

  edit_recipiant.value = pre_recipiant.value
  edit_rec_tele.value = pre_rec_tele.value
  edit_address.value = pre_address.value
  set_this_add_default.value = false
  edit_address_id.value = address.id
  edit_set_this_add_default.value = pre_is_default.value
}

// 获取用户的所有收货地址
function getAllAddresses() {
  address_lst.value = []
  console.log("get all address: ", userId)
  axiosInstance.get(`/user/showPersonalDeliveryAddress?userId=${userId}`, {}).then((response) => {
    const r = response.data['data']
    console.log('get user delivery address: ', r);
    r.forEach(function (item) {
      // console.log(item);
      if (item !== null) {
        address_lst.value.push(item);
      }
    });
  });
  console.log("All address list: ", address_lst.value)
  for (var i = 0; i < address_lst.value.length; i++) {
    add = address_lst.value[i]
    console.log("check delivery address: ", add)
  }
}

function showNotPaidOrder() {
  not_paid_order.value = true
  not_delivered_order.value = false
  delivering_order.value = false
  completed_order.value = false
  refunded_order.value = false
  refunding_order.value = false
  cancel_order.value = false
  unpaid_current_page = 1
  getUnpaidOrder(unpaid_current_page)
}

function showNotDelivered() {
  not_paid_order.value = false
  not_delivered_order.value = true
  delivering_order.value = false
  completed_order.value = false
  refunded_order.value = false
  refunding_order.value = false
  cancel_order.value = false
  not_delivered_current_page = 1

  getNotdeliveredOrder(not_delivered_current_page)
}

function showDelivering() {
  not_paid_order.value = false
  not_delivered_order.value = false
  delivering_order.value = true
  completed_order.value = false
  refunded_order.value = false
  refunding_order.value = false
  cancel_order.value = false
  delivering_current_page = 1
  getDeliveringOrder(delivering_current_page)
}

function showCompleted() {
  not_paid_order.value = false
  not_delivered_order.value = false
  delivering_order.value = false
  completed_order.value = true
  refunded_order.value = false
  refunding_order.value = false
  cancel_order.value = false
  finished_current_page = 1
  getFinishedOrder(finished_current_page)
}

function showRefundedOrder() {
  not_paid_order.value = false
  not_delivered_order.value = false
  delivering_order.value = false
  completed_order.value = false
  refunded_order.value = true
  refunding_order.value = false
  cancel_order.value = false
  refunded_current_page = 1
  getRefundedOrder(refunded_current_page)
}

function showRefundingOrder() {
  not_paid_order.value = false
  not_delivered_order.value = false
  delivering_order.value = false
  completed_order.value = false
  refunded_order.value = false
  refunding_order.value = true
  cancel_order.value = false
  refunding_current_page = 1
  getRefundingOrder(refunding_current_page)
}

function showCancelOrder() {
  not_paid_order.value = false
  not_delivered_order.value = false
  delivering_order.value = false
  completed_order.value = false
  refunded_order.value = false
  refunding_order.value = false
  cancel_order.value = true
  cancelled_current_page = 1
  getCancelledOrder(cancelled_current_page)
}

function getUnpaidOrder(unpaid_current_page) {
  unpaid_order_lst.value = [];
  const pageSize = 5;
  not_paid_order_loading.value = true
  console.log("Show user id when getting unpaid order: ", userId, "\npage no: ", unpaid_current_page);
  axiosInstance.get('/userOrder/showUnpaidOrder', {
    params: {
      userId: userId,
      pageNo: unpaid_current_page,
      pageSize: pageSize,
    }
  }).then((response) => {
    const unpaid_orders = response.data.data.userOrderPlusList; // 获取订单数据
    console.log("unpaid order 请求数据: ", response)
    unpaid_orders.forEach(function (item) {
      // console.log("[unpaid orders] item: ", item)
      unpaid_order_lst.value.push(item);
    });
    unpaid_total_page.value = response.data.data.pages; // 总页数
    console.log("[unpaid order] set total page: ", unpaid_total_page.value)
    not_paid_order_loading.value = false
  });
  console.log("unpaid orders: ", unpaid_order_lst.value)
}

function getCancelledOrder(cancelled_current_page) {
  cancelled_order_lst.value = [];
  const pageSize = 5;
  cancel_order_loading.value = true
  console.log("Show user id when getting cancelled order: ", userId, "\npage no: ", cancelled_current_page);
  axiosInstance.get('/userOrder/showCancelledOrder', {
    params: {
      userId: userId,
      pageNo: cancelled_current_page,
      pageSize: pageSize,
    }
  }).then((response) => {
    const cancelled_orders = response.data.data.userOrderPlusList; // 获取订单数据
    console.log("cancelled order 请求数据: ", response)
    cancelled_orders.forEach(function (item) {
      // console.log("[unpaid orders] item: ", item)
      cancelled_order_lst.value.push(item);
    });
    cancelled_total_page.value = response.data.data.pages; // 总页数
    console.log("[cancelled order] set total page: ", cancelled_total_page.value)
    cancel_order_loading.value = false
  });
  console.log("cancelled orders: ", cancelled_order_lst.value)
}

function getFinishedOrder(finished_current_page) {
  finished_order_lst.value = [];
  const pageSize = 5;
  finished_order_loading.value = true

  console.log("Show user id when getting finished order: ", userId, "\npage no: ", finished_current_page);
  axiosInstance.get('/userOrder/showFinishedOrder', {
    params: {
      userId: userId,
      pageNo: finished_current_page,
      pageSize: pageSize,
    }
  }).then((response) => {
    const finished_orders = response.data.data.userOrderPlusList; // 获取订单数据
    console.log("finised order 请求数据: ", response)
    finished_orders.forEach(function (item) {
      // console.log("[unpaid orders] item: ", item)
      finished_order_lst.value.push(item);
    });
    finished_total_page.value = response.data.data.pages; // 总页数
    console.log("[finished order] set total page: ", finished_total_page.value)
    finished_order_loading.value = false
  });
  console.log("finished orders: ", finished_order_lst.value)
}

function getRefundedOrder(refunded_current_page) {
  refunded_order_lst.value = [];
  const pageSize = 5;
  refunded_order_loading.value = true
  console.log("Show user id when getting refunded order: ", userId, "\npage no: ", refunded_current_page);
  axiosInstance.get('/userOrder/showRefundedOrder', {
    params: {
      userId: userId,
      pageNo: refunded_current_page,
      pageSize: pageSize,
    }
  }).then((response) => {
    const refunded_orders = response.data.data.userOrderPlusList; // 获取订单数据
    console.log("refunded order 请求数据: ", response)
    refunded_orders.forEach(function (item) {
      // console.log("[unpaid orders] item: ", item)
      refunded_order_lst.value.push(item);
    });
    refunded_total_page.value = response.data.data.pages; // 总页数
    console.log("[refunded order] set total page: ", refunded_total_page.value)
    refunded_order_loading.value = false
  });
  console.log("refunded orders: ", refunded_order_lst.value)
}

function getRefundingOrder(refunding_current_page) {
  refunding_order_lst.value = [];
  const pageSize = 5;
  refunding_order_loading.value = true
  console.log("Show user id when getting refunding order: ", userId, "\npage no: ", refunding_current_page);
  axiosInstance.get('/userOrder/showRefundingOrder', {
    params: {
      userId: userId,
      pageNo: refunding_current_page,
      pageSize: pageSize,
    }
  }).then((response) => {
    const refunding_orders = response.data.data.userOrderPlusList; // 获取订单数据
    console.log("refunding order 请求数据: ", response)
    refunding_orders.forEach(function (item) {
      // console.log("[unpaid orders] item: ", item)
      refunding_order_lst.value.push(item);
    });
    refunding_total_page.value = response.data.data.pages; // 总页数
    console.log("[refunding order] set total page: ", refunding_total_page.value)
    refunding_order_loading.value = false
  });
  console.log("refunding orders: ", refunding_order_lst.value)
}

function getDeliveringOrder(delivering_current_page) {
  delivering_order_lst.value = [];
  const pageSize = 5;
  delivering_order_loading.value = true
  console.log("Show user id when getting delivering order: ", userId, "\npage no: ", delivering_current_page);
  axiosInstance.get('/userOrder/showShippedOrder', {
    params: {
      userId: userId,
      pageNo: delivering_current_page,
      pageSize: pageSize,
    }
  }).then((response) => {
    const delivering_orders = response.data.data.userOrderPlusList; // 获取订单数据
    console.log("delivering order 请求数据: ", response)
    delivering_orders.forEach(function (item) {
      // console.log("[unpaid orders] item: ", item)
      delivering_order_lst.value.push(item);
    });
    delivering_total_page.value = response.data.data.pages; // 总页数
    console.log("[delivering order] set total page: ", delivering_total_page.value)
    delivering_order_loading.value = false
  });
  console.log("delivering orders: ", delivering_order_lst.value)
}

function getNotdeliveredOrder(not_delivered_current_page) {
  not_delivered_order_lst.value = [];
  const pageSize = 5;
  not_delivered_order_loading.value = true
  console.log("Show user id when getting not delivered order: ", userId, "\npage no: ", not_delivered_current_page);
  axiosInstance.get('/userOrder/showPaidOrder', {
    params: {
      userId: userId,
      pageNo: not_delivered_current_page,
      pageSize: pageSize,
    }
  }).then((response) => {
    const not_delivered_orders = response.data.data.userOrderPlusList; // 获取订单数据
    console.log("not delivered order 请求数据: ", response)
    not_delivered_orders.forEach(function (item) {
      console.log("not delivered order item: ", item)
      not_delivered_order_lst.value.push(item);
    });
    not_delivered_total_page.value = response.data.data.pages; // 总页数
    console.log("[not delivered order] set total page: ", not_delivered_order_lst.value)
    not_delivered_order_loading.value = false
  });

}

function confirmDeleteOrder(order) {
  console.log("确认删除订单: ", order)
  delete_order_id.value = order.userOrder.id

  delete_order_shopName.value = order.shop.shopName
  delete_order_goodsName.value = order.goods.goodsName
  delete_order_img.value = order.goods.image
  delete_order_quantity.value = order.userOrder.quantity
  delete_order_totalPrice.value = order.userOrder.totalPrice

  delete_order_recipiant.value = order.deliveryAddress.name
  delete_order_rec_tele.value = order.deliveryAddress.phoneNumber
  delete_order_rec_address.value = order.deliveryAddress.address

  console.log("确认删除订单信息传递：\n订单编号", delete_order_id,
    "\n商店名：", delete_order_shopName,
    "\n商品名：", delete_order_goodsName,
    "\n数量：", delete_order_quantity,
    "\n实付款：", delete_order_totalPrice,
    "\n收货人：", delete_order_recipiant,
    "\n联系方式：", delete_order_rec_tele,
    "\n地址：", delete_order_rec_address.value)

  delete_order_layout.value = true
}

function confirmCancelOrder(order) {
  console.log("确认取消订单: ", order)
  cancel_order_id.value = order.userOrder.id

  cancel_order_shopName.value = order.shop.shopName
  cancel_order_goodsName.value = order.goods.goodsName
  cancel_order_img.value = order.goods.image
  cancel_order_quantity.value = order.userOrder.quantity
  cancel_order_totalPrice.value = order.userOrder.totalPrice

  cancel_order_recipiant.value = order.deliveryAddress.name
  cancel_order_rec_tele.value = order.deliveryAddress.phoneNumber
  cancel_order_rec_address.value = order.deliveryAddress.address

  console.log("确认取消订单信息传递：\n订单编号", cancel_order_id,
    "\n商店名：", cancel_order_shopName,
    "\n商品名：", cancel_order_goodsName,
    "\n数量：", cancel_order_quantity,
    "\n实付款：", cancel_order_totalPrice,
    "\n收货人：", cancel_order_recipiant,
    "\n联系方式：", cancel_order_rec_tele,
    "\n地址：", cancel_order_rec_address.value)

  cancel_order_layout.value = true
}

function confirmRefundOrder(order) {
  console.log("确认退货退款: ", order)

  if (order.userOrder.unitPrice !== order.goods.price) {
    operation_fail_alert_layout.value = true
    operation_fail_message.value = "申请退货退款失败，活动商品无法退货退款！"
    refund_order_layout.value = false
  } else {
    refund_order_id.value = order.userOrder.id

    refund_order_shopName.value = order.shop.shopName
    refund_order_goodsName.value = order.goods.goodsName
    refund_order_img.value = order.goods.image
    refund_order_quantity.value = order.userOrder.quantity
    refund_order_totalPrice.value = order.userOrder.totalPrice

    refund_order_recipiant.value = order.deliveryAddress.name
    refund_order_rec_tele.value = order.deliveryAddress.phoneNumber
    refund_order_rec_address.value = order.deliveryAddress.address

    console.log("确认退货退款信息传递：\n订单编号", refund_order_id,
      "\n商店名：", refund_order_shopName,
      "\n商品名：", refund_order_goodsName,
      "\n数量：", refund_order_quantity,
      "\n实付款：", refund_order_totalPrice,
      "\n收货人：", refund_order_recipiant,
      "\n联系方式：", refund_order_rec_tele,
      "\n地址：", refund_order_rec_address.value)

    refund_order_layout.value = true
  }
}

function confirmPayOrder(order) {
  console.log("确认支付: ", order)
  pay_order.value = order
  console.log("确认支付信息传递：", pay_order.value)
  pay_order_layout.value = true
}

function payOrder(order) {
  console.log("支付订单: ", order)
  let pay_order_obj = {
    "addressId": order.deliveryAddress.id,
    "date": order.userOrder.date,
    "eventId": order.userOrder.eventId,
    "goodsId": order.userOrder.goodsId,
    "id": order.userOrder.id,
    "quantity": order.userOrder.quantity,
    "shopId": order.userOrder.shopId,
    "totalPrice": order.userOrder.totalPrice,
    "unitPrice": order.userOrder.unitPrice,
    "userId": order.userOrder.userId,
    "status": order.userOrder.status,
  }
  console.log("下单obj：", pay_order_obj)
  let pay_list = []
  pay_list.push(pay_order_obj)
  console.log("下单list：", pay_list)
  axiosInstance.post('/userOrder/pay', pay_list).then((response) => {
    console.log("用户支付待支付订单返回数据：", response)
    if (response.data['code'] === 20000) {
      operation_success_alert_layout.value = true
      operation_success_message.value = '订单已支付'
      delete_order_layout.value = false
    } else {
      operation_fail_message.value = response.data['message']
      operation_fail_alert_layout.value = true
      delete_order_layout.value = false
    }
  });
}

// 用户删除订单
function deleteOrder(del_order_id) {
  console.log("删除订单: ", del_order_id)
  axiosInstance.put('/userOrder/deleteFinishedOrder', {
    orderId: del_order_id
  }, {
    params: {
      orderId: del_order_id
    }
  }).then((response) => {
    console.log("用户删除订单返回数据：", response)
    if (response.data['code'] === 20000) {
      operation_success_alert_layout.value = true
      operation_success_message.value = '已删除订单'
      delete_order_layout.value = false
    } else {
      operation_fail_message.value = response.data['message']
      operation_fail_alert_layout.value = true
      delete_order_layout.value = false
    }
  });
}

// 用户退款
function refunding(orderId) {
  console.log("User refunding: ", orderId)

  axiosInstance.put('/userOrder/refunding', {
    orderId: orderId
  }, {
    params: {
      orderId: orderId
    }
  }).then((response) => {
    console.log("申请退货退款：", response)
    if (response.data['code'] === 20000) {
      operation_success_alert_layout.value = true
      operation_success_message.value = '已申请退货退款，等待商家同意'
      refund_order_layout.value = false
    } else {
      operation_fail_message.value = response.data['message']
      operation_fail_alert_layout.value = true
      refund_order_layout.value = false
    }
  });
}

// 用户取消订单
function cancelOrder(orderId) {
  console.log("User cancel order: ", orderId)
  axiosInstance.put('/userOrder/cancel', {
    orderId: orderId
  }, {
    params: {
      orderId: orderId
    }
  }).then((response) => {
    console.log("取消订单：", response)
    if (response.data['code'] === 20000) {
      operation_success_alert_layout.value = true
      operation_success_message.value = '已取消订单'
      cancel_order_layout.value = false
    } else {
      operation_fail_message.value = response.data['message']
      operation_fail_alert_layout.value = true
      cancel_order_layout.value = false
    }
  });
}

// 用户确认收货
function ensureReceiveOrder(order) {
  let current_time = getFormattedDate()
  console.log("User confirm receiving: \norder实例：", order,
    "\naddressId: ", order.deliveryAddress.id,
    "\ndate: ", order.userOrder.date,
    "\nevent Id: ", order.userOrder.eventId,
    "\ngoods Id: ", order.goods.id,
    "\norder id: ", order.userOrder.id,
    "\nquantity: ", order.userOrder.quantity,
    "\nshop id: ", order.shop.id,
    "\ntotal price: ", order.userOrder.totalPrice,
    "\nunit price: ", order.userOrder.unitPrice,
    "\nuser id: ", order.userOrder.userId,
    "\ncurrent time: ", current_time)
  axiosInstance.put('/userOrder/receive', {
    addressId: order.deliveryAddress.id,
    date: order.userOrder.date,
    eventId: order.userOrder.eventId,
    goodsId: order.goods.id,
    id: order.userOrder.id,
    quantity: order.userOrder.quantity,
    shopId: order.shop.id,
    totalPrice: order.userOrder.totalPrice,
    unitPrice: order.userOrder.unitPrice,
    userId: order.userOrder.userId,
  }, {
    params: {
      stime: current_time,
    }
  }).then((response) => {
    console.log("用户确认收货返回数据：", response)
    if (response.data['code'] === 20000) {
      operation_success_alert_layout.value = true
      operation_success_message.value = '确认收货成功'
    } else {
      operation_fail_message.value = response.data['message']
      operation_fail_alert_layout.value = true
    }
  });
}

</script>

<style lang="sass">
.GNL

    &__toolbar
        height: 64px

    &__toolbar-input
        width: 55%

    &__drawer-item
        line-height: 24px
        border-radius: 0 24px 24px 0
        margin-right: 12px

    .q-item__section--avatar
        .q-icon
            color: #5f6368

    .q-item__label
        color: #3c4043
        letter-spacing: .01785714em
        font-size: .875rem
        font-weight: 500
        line-height: 1.25rem

    &__drawer-footer-link
        color: inherit
        text-decoration: none
        font-weight: 500
        font-size: .75rem

    &:hover
        color: #000
</style>

<style scoped>
.personalInfo-display-change {
  display: flex;
  align-items: center;
  margin-left: 3%;
}

.profile-display-box {
  max-width: 100%;
  width: 100%;
  display: flex;
}

dt {
  margin: 0 0 6px;
  max-width: 100%;
  width: 100%;
}

dd {
  margin-left: 0%;
  max-width: 100%;
  width: 100%;
}

.label {
  display: flex;
  align-items: center;
  font-weight: 500;
  font-size: 18px;
}

.username-inputbox,
.phone-inputbox,
.email-inputbox,
.shop-inputbox,
.shopIntro-inputbox {
  max-width: 100%;
  width: 100%;
  height: 75px;
  font-size: 14px;
  font-family: system-ui, -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen, Ubuntu, Cantarell, 'Open Sans', 'Helvetica Neue', sans-serif;
}

.profilePicture {
  margin-left: 7%;
  width: 50%;
  max-width: 100%;
  display: flex;
}

.note {
  font-family: system-ui, -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen, Ubuntu, Cantarell, 'Open Sans', 'Helvetica Neue', sans-serif;
  font-size: smaller;
  color: #414141;
}

.submit-button button,
.edit-button button {
  fill: azure;
  background-color: rgba(127, 82, 190, 0.837);
  color: azure;
  cursor: pointer;
  border-radius: 6px;
  border: 0;
  display: flex;
  align-items: center;
  height: 32px;
  min-width: max-content;
  padding: 0 12px;
  margin-bottom: 10%;
}

.button-content {
  font-size: 15px;
  font-weight: 8px;
}

.profile-img {
  width: 230px;
  max-width: 100%;
  position: relative;
  z-index: 0;
}

.profile-edit {
  margin-top: -50px;
  position: relative;
  z-index: 1;
}

.my-card {
  width: 100%;
  max-width: 350px;
}
</style>
