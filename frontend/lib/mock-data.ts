import { Coupon, Product, Review, Shop, User } from "./types";

export const categories = ["手机数码", "居家生活", "服饰鞋包", "食品生鲜", "美妆个护", "运动户外", "母婴玩具", "家用电器"];

export const users: User[] = [
  { id: 1, name: "陈小优", account: "user@example.com", role: "USER" },
  { id: 2, name: "林掌柜", account: "merchant@example.com", role: "MERCHANT" },
  { id: 3, name: "平台运营", account: "admin@example.com", role: "ADMIN" },
];

export const shops: Shop[] = [
  { id: 1, name: "橙意生活馆", ownerId: 2, slogan: "把日常过成轻松的小确幸", score: 4.9, fans: 18420, category: "居家生活" },
  { id: 2, name: "云集数码铺", ownerId: 2, slogan: "好用数码，清爽到手", score: 4.8, fans: 26310, category: "手机数码" },
  { id: 3, name: "山野补给社", ownerId: 2, slogan: "通勤与周末都能出发", score: 4.7, fans: 9750, category: "运动户外" },
  { id: 4, name: "鲜时厨房", ownerId: 2, slogan: "今晚吃点真的好东西", score: 4.9, fans: 22680, category: "食品生鲜" },
];

export const products: Product[] = [
  {
    id: 101,
    shopId: 2,
    title: "星河 Pro 无线降噪耳机",
    category: "手机数码",
    price: 299,
    originPrice: 399,
    sales: 4200,
    rating: 4.9,
    stock: 620,
    image: "linear-gradient(135deg,#fb923c,#2563eb)",
    tags: ["限时券", "低延迟", "长续航"],
    skus: ["暖沙白", "曜石黑", "雾蓝"],
    description: "自适应降噪、通透模式和 32 小时综合续航，适合通勤、学习和轻运动场景。",
  },
  {
    id: 102,
    shopId: 1,
    title: "云朵感四件套 纯棉亲肤款",
    category: "居家生活",
    price: 189,
    originPrice: 269,
    sales: 3180,
    rating: 4.8,
    stock: 980,
    image: "linear-gradient(135deg,#fdba74,#a7f3d0)",
    tags: ["满减", "可机洗", "亲肤"],
    skus: ["奶油米", "浅雾灰", "杏桃橙"],
    description: "高支棉面料，触感柔软，颜色清爽，适合四季卧室搭配。",
  },
  {
    id: 103,
    shopId: 4,
    title: "低温烘焙坚果礼盒 1.2kg",
    category: "食品生鲜",
    price: 128,
    originPrice: 168,
    sales: 8600,
    rating: 4.9,
    stock: 430,
    image: "linear-gradient(135deg,#f97316,#facc15)",
    tags: ["热卖", "少盐", "礼盒"],
    skus: ["经典款", "轻甜款", "家庭分享款"],
    description: "多种坚果科学配比，低温烘焙锁住香气，送礼和办公室补给都合适。",
  },
  {
    id: 104,
    shopId: 3,
    title: "轻量防泼水城市双肩包",
    category: "运动户外",
    price: 219,
    originPrice: 299,
    sales: 1950,
    rating: 4.7,
    stock: 360,
    image: "linear-gradient(135deg,#fb923c,#64748b)",
    tags: ["通勤", "防泼水", "大容量"],
    skus: ["18L 黑", "18L 灰", "22L 绿"],
    description: "分仓收纳、轻量背负和防泼水面料，覆盖通勤、短途出行和健身场景。",
  },
  {
    id: 105,
    shopId: 1,
    title: "桌面无线快充氛围灯",
    category: "家用电器",
    price: 159,
    originPrice: 219,
    sales: 2760,
    rating: 4.6,
    stock: 510,
    image: "linear-gradient(135deg,#fed7aa,#38bdf8)",
    tags: ["新品", "三档调光", "快充"],
    skus: ["白色", "银灰", "木纹"],
    description: "灯光、无线充电和小物收纳三合一，适合床头、书桌和办公位。",
  },
  {
    id: 106,
    shopId: 4,
    title: "冷萃咖啡液 12 杯装",
    category: "食品生鲜",
    price: 69,
    originPrice: 89,
    sales: 5200,
    rating: 4.8,
    stock: 720,
    image: "linear-gradient(135deg,#92400e,#f97316)",
    tags: ["第二件半价", "0 蔗糖", "便携"],
    skus: ["醇厚黑咖", "燕麦拿铁", "榛果风味"],
    description: "即开即饮，也可搭配牛奶和冰块，适合快节奏工作日。",
  },
];

export const coupons: Coupon[] = [
  { id: 1, code: "YX30", title: "满 199 减 30", threshold: 199, amount: 30, enabled: true },
  { id: 2, code: "YX80", title: "满 599 减 80", threshold: 599, amount: 80, enabled: true },
];

export const reviews: Review[] = [
  { id: 1, productId: 101, user: "晴天买手", rating: 5, content: "降噪很稳，通勤地铁里听播客舒服很多。", createdAt: "2026-05-01" },
  { id: 2, productId: 102, user: "南方小屋", rating: 5, content: "颜色比想象中耐看，洗完没有明显缩水。", createdAt: "2026-04-28" },
  { id: 3, productId: 103, user: "办公室零食官", rating: 5, content: "坚果新鲜，分量足，礼盒质感也不错。", createdAt: "2026-04-26" },
];
