package com.haoyigou.hyg.entity;

import java.util.List;

/**
 * Created by wuliang on 2017/3/31.
 * <p>
 * 充值数据封装bean
 */

public class VoucherEntry {



        /**
         * disprice : 0.01
         * id : 19
         * isShowIcon : 1
         * ordertype : 01
         * price : 10.0
         * pricetemp : 0.01
         * recharge : 10
         * rechargeName : 10元
         * skulist : [{"disprice":10,"id":23,"memo":"全国流量，当月可用","region":0},{"disprice":14.7,"id":48,"memo":"省内流量，当月可用","region":1}]
         */

        private double disprice;
        private int id;
        private int isShowIcon;
        private String ordertype;
        private double price;
        private double pricetemp;
        private int recharge;
        private String rechargeName;
        private List<SkulistBo> skulist;
        private int hasMoneyCoupon;
        private int hasflowCoupon;
        private int parprice;

    public int getParprice() {
        return parprice;
    }

    public void setParprice(int parprice) {
        this.parprice = parprice;
    }

    public int getHasMoneyCoupon() {
        return hasMoneyCoupon;
    }

    public void setHasMoneyCoupon(int hasMoneyCoupon) {
        this.hasMoneyCoupon = hasMoneyCoupon;
    }

    public int getHasflowCoupon() {
        return hasflowCoupon;
    }

    public void setHasflowCoupon(int hasflowCoupon) {
        this.hasflowCoupon = hasflowCoupon;
    }

    public double getDisprice() {
            return disprice;
        }

        public void setDisprice(double disprice) {
            this.disprice = disprice;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getIsShowIcon() {
            return isShowIcon;
        }

        public void setIsShowIcon(int isShowIcon) {
            this.isShowIcon = isShowIcon;
        }

        public String getOrdertype() {
            return ordertype;
        }

        public void setOrdertype(String ordertype) {
            this.ordertype = ordertype;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public double getPricetemp() {
            return pricetemp;
        }

        public void setPricetemp(double pricetemp) {
            this.pricetemp = pricetemp;
        }

        public int getRecharge() {
            return recharge;
        }

        public void setRecharge(int recharge) {
            this.recharge = recharge;
        }

        public String getRechargeName() {
            return rechargeName;
        }

        public void setRechargeName(String rechargeName) {
            this.rechargeName = rechargeName;
        }

        public List<SkulistBo> getSkulist() {
            return skulist;
        }

        public void setSkulist(List<SkulistBo> skulist) {
            this.skulist = skulist;
        }

        public static class SkulistBo {
            /**
             * disprice : 10.0
             * id : 23
             * memo : 全国流量，当月可用
             * region : 0
             */

            private double disprice;
            private int id;
            private String memo;
            private int region;

            public double getDisprice() {
                return disprice;
            }

            public void setDisprice(double disprice) {
                this.disprice = disprice;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getMemo() {
                return memo;
            }

            public void setMemo(String memo) {
                this.memo = memo;
            }

            public int getRegion() {
                return region;
            }

            public void setRegion(int region) {
                this.region = region;
            }
        }
    }

